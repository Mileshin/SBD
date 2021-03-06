package astoria.dummymaker.export.asstring;

import io.dummymaker.data.Dummy;
import io.dummymaker.export.IExporter;
import io.dummymaker.export.impl.JsonExporter;
import io.dummymaker.export.naming.Strategies;
import io.dummymaker.export.validators.JsonValidator;
import io.dummymaker.factory.IProduceFactory;
import io.dummymaker.factory.impl.GenProduceFactory;
import org.junit.Test;

import java.util.List;

/**
 * "Default Description"
 *
 * @author GoodforGod
 * @since 20.08.2017
 */
public class JsonExportAsStringTest extends StringExportAssert {

    private final IProduceFactory produceFactory = new GenProduceFactory();

    private final JsonValidator validation = new JsonValidator();

    public JsonExportAsStringTest() {
        super(new JsonExporter().withPretty().withPath(null).withStrategy(null).withPath("            "),
                new JsonValidator(), 5, 14);
    }

    @Test
    public void exportListOfDummiesInJsonWithNamingStrategy() throws Exception {
        final Strategies strategy = Strategies.UNDERSCORED_UPPER_CASE;

        final List<Dummy> dummy = produceFactory.produce(Dummy.class, 2);
        final IExporter exporter = new JsonExporter().withStrategy(strategy.getStrategy()).withPretty().withStrategy(null);

        final String dummyAsString = exporter.exportAsString(dummy);
        assertNotNull(dummyAsString);

        final String[] jsonArray = dummyAsString.split("\n");
        assertEquals(14, jsonArray.length);

        validation.isTwoDummiesValidWithNamingStrategy(jsonArray, strategy.getStrategy());
    }
}
