package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.mockito.Mock;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import static org.junit.Assert.*;

public class AddProductCommandHandlerTest {


    AddProductCommandHandler addProductCommandHandler;

    @Mock
    ReservationRepository reservationRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    SuggestionService suggestionService;
    @Mock
    ClientRepository clientRepository;
    @Mock
    SystemContext systemContext;

    @Before
    public void setup(){
        addProductCommandHandler = new AddProductCommandHandler(reservationRepository, productRepository, suggestionService, clientRepository, systemContext);
    }

}