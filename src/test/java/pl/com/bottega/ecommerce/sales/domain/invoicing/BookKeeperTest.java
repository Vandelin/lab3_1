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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}