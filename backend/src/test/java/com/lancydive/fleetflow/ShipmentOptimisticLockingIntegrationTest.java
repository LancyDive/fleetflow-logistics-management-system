package com.lancydive.fleetflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.lancydive.fleetflow.constants.ShipmentStatus;
import com.lancydive.fleetflow.entity.Shipment;
import com.lancydive.fleetflow.repository.ShipmentRepository;

@SpringBootTest
class ShipmentOptimisticLockingIntegrationTest {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void staleShipmentUpdateShouldFailWithOptimisticLockingException() {

        TransactionTemplate transactionTemplate =
                new TransactionTemplate(transactionManager);

        /*
         * Create a temporary shipment for this test.
         */
        Shipment createdShipment = transactionTemplate.execute(status -> {

            Shipment shipment = Shipment.builder()
                    .trackingNumber("TEST-" + System.currentTimeMillis())
                    .pickupAddress("Pune")
                    .deliveryAddress("Nashik")
                    .weightKg(1000.0)
                    .status(ShipmentStatus.CREATED)
                    .active(true)
                    .build();

            return shipmentRepository.saveAndFlush(shipment);
        });

        assertNotNull(createdShipment);

        Long shipmentId = createdShipment.getId();

        try {

            /*
             * Simulate two different transactions reading
             * the same shipment.
             */
            Shipment firstRead = transactionTemplate.execute(status ->
                    shipmentRepository.findById(shipmentId)
                            .orElseThrow());

            Shipment secondRead = transactionTemplate.execute(status ->
                    shipmentRepository.findById(shipmentId)
                            .orElseThrow());

            assertEquals(0L, firstRead.getVersion());
            assertEquals(0L, secondRead.getVersion());

            /*
             * First transaction updates the shipment.
             * Version should become 1.
             */
            firstRead.setPickupAddress("Mumbai");

            transactionTemplate.execute(status ->
                    shipmentRepository.saveAndFlush(firstRead));

            /*
             * The second object still contains version 0.
             * Trying to save it should fail because the database
             * now contains version 1.
             */
            secondRead.setPickupAddress("Delhi");

            assertThrows(
                    ObjectOptimisticLockingFailureException.class,
                    () -> transactionTemplate.execute(status ->
                            shipmentRepository.saveAndFlush(secondRead))
            );

            /*
             * Verify that the first update remained in the database
             * and the stale second update did not overwrite it.
             */
            Shipment finalShipment =
                    transactionTemplate.execute(status ->
                            shipmentRepository.findById(shipmentId)
                                    .orElseThrow());

            assertEquals("Mumbai", finalShipment.getPickupAddress());
            assertEquals(1L, finalShipment.getVersion());

        } finally {

            /*
             * Remove the temporary test data.
             */
            transactionTemplate.executeWithoutResult(status ->
                    shipmentRepository.deleteById(shipmentId));
        }
    }
}