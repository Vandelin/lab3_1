package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookKeeperTest {

    @Mock
    TaxPolicy taxPolicy;

    private InvoiceRequest invoiceRequest;
    private BookKeeper bookKeeper;


    @Before
    public void setup() {
        invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Peter"));
        bookKeeper = new BookKeeper(new InvoiceFactory());

        when(taxPolicy.calculateTax(anyObject(), anyObject())).thenReturn(new Tax(Money.ZERO, "standard"));

    }

    @Test
    public void givenInvoiceRequestOneItemWhenIssuanceThenInvoiceWithOneItem() {

        invoiceRequest.add(new RequestItem(mock(ProductData.class), 10, Money.ZERO));

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        Assert.assertThat(invoice.getItems().size(), is(1));

    }

    @Test
    public void givenInvoiceRequestTwoItemsWhenIssuanceThenTaxPolicyMethodTwoTimes() {
        invoiceRequest.add(new RequestItem(mock(ProductData.class), 10, Money.ZERO));
        invoiceRequest.add(new RequestItem(mock(ProductData.class), 10, Money.ZERO));

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(anyObject(), anyObject());

    }

    @Test
    public void givenInvoiceRequestZeroItemsWhenIssuanceThenInvoiceWithZeroItems() {
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        Assert.assertThat(invoice.getItems().size(), is(0));
    }

    @Test
    public void givenInvoiceRequestMoneyTenWhenIssuanceThenInvoiceHaveGrosTen() {
        invoiceRequest.add(new RequestItem(mock(ProductData.class), 20, new Money(10)));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        Assert.assertTrue(invoice.getGros().equals(new Money(10)));


    }

    @Test
    public void givenInvoiceRequestQuantityTwentyWhenIssuanceThenInvoiceItemQuantityTwenty() {
        invoiceRequest.add(new RequestItem(mock(ProductData.class), 20, new Money(10)));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        Assert.assertThat(invoice.getItems().get(0).getQuantity(), is(20));

    }

    @Test
    public void givenInvoiceFactoryWhenIssuanceThenInvoiceFactoryCalledOnce(){
        ClientData clientData = mock(ClientData.class);
        InvoiceFactory invoiceFactory = mock(InvoiceFactory.class);
        when(invoiceFactory.create(clientData)).thenReturn(null);
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(invoiceFactory, times(1)).create(any(ClientData.class));
    }

    @Test
    public void givenInvoiceFactoryWhenIssuanceThenNeverCallCalculateTaxMethod(){
        InvoiceFactory invoiceFactory = mock(InvoiceFactory.class);
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(0)).calculateTax(anyObject(),anyObject());
    }
}