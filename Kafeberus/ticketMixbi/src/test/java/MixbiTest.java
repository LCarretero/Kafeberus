import com.hiberus.avro.*;
import com.hiberus.services.TicketMixbiService;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MixbiTest {
    @Test
    public void testProcess() {
        // Create mock streams
        KStream<TableKey, UserInTicketValue> userStream = mock(KStream.class);
        KTable<TableKey, ProductsInTicketValue> productTable = mock(KTable.class);

        // Create test data
        TableKey tableKey = new TableKey("test");
        UserInTicketValue userInTicketValue = new UserInTicketValue("userTicket","ticketId" ,true);
        ProductsInTicketValue productsInTicketValue = new ProductsInTicketValue("ticketId",Collections.emptyMap(), 10D);
        TicketKey ticketKey = new TicketKey("test");
        FinalTicket finalTicket = FinalTicket.newBuilder()
                .setMapOfProducts(productsInTicketValue.getMapOfProducts())
                .setIdUser(userInTicketValue.getIdUser())
                .setRewarded(userInTicketValue.getRewarded())
                .setPrice(userInTicketValue.getRewarded() ? productsInTicketValue.getTotalPrice() - 5
                        : productsInTicketValue.getTotalPrice())
                .setTimeStamp("test")
                .build();

        // Mock stream behavior
        when(userStream.map((k, v) -> KeyValue.pair(ticketKey, v)).selectKey((k, v) -> ticketKey)).thenReturn(mock(KStream.class));
        when(userStream.join(productTable, (u, p) -> finalTicket)).thenReturn(mock(KStream.class));

        // Create service instance
        TicketMixbiService service = new TicketMixbiService();

        // Call method under test
        KStream<TicketKey, FinalTicket> result = service.process().apply(userStream, (KStream<TableKey, ProductsInTicketValue>) productTable);

        // Verify result
        Assertions.assertNotNull(result); // Change to check if result is not null
    }
}
