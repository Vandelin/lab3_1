package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddProductCommandHandlerTest {

    AddProductCommand addProductCommand;
    AddProductCommandHandler addProductCommandHandler;
    SystemContext systemContext;
    Product product;
    Product product2;
    Reservation reservation;
    Client client;

    @Mock
    ReservationRepository reservationRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    SuggestionService suggestionService;
    @Mock
    ClientRepository clientRepository;

    @Before
    public void setup() {
        systemContext = new SystemContext();
        addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), 10);
        product = new Product(Id.generate(), new Money(3.34, Money.DEFAULT_CURRENCY), "tempId", ProductType.FOOD);
        product2 = new Product(Id.generate(), new Money(3.35, Money.DEFAULT_CURRENCY), "tempId", ProductType.FOOD);


        reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, mock(ClientData.class), new Date());



        when(reservationRepository.load(any())).thenReturn(reservation);
        when(productRepository.load(any())).thenReturn(product);
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(product2);
        addProductCommandHandler = new AddProductCommandHandler(reservationRepository, productRepository, suggestionService, clientRepository, systemContext);
    }

    @Test
    public void givenProductWhenHandleThenMethodReturnsTrue() {
        Assert.assertTrue(product.isAvailable());
    }

    @Test
    public void givenAddProductCommandHandlerOnceWhenHandleThenProductLoadOnce(){
        addProductCommandHandler.handle(addProductCommand);
        verify(productRepository, times(1)).load(addProductCommand.getProductId());
    }

    @Test
    public void givenAddProductCommandHandlerTwiceWhenHandleThenSuggestEquivalentTwoTimes(){
        product.markAsRemoved();
        addProductCommandHandler.handle(addProductCommand);
        addProductCommandHandler.handle(addProductCommand);
        verify(suggestionService, times(2)).suggestEquivalent(product, client);
    }


}
