package astoria.dummymaker.factory;

import io.dummymaker.data.*;
import io.dummymaker.factory.impl.GenProduceFactory;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Default Comment
 *
 * @author GoodforGod
 * @since 31.07.2017
 */
public class ProduceFactoryTest {

    @Test
    public void produceLessThanZeroAmount() {
        final IProduceFactory dummyGenPopulateFactory = new GenProduceFactory();
        final List<Dummy> dummies = dummyGenPopulateFactory.produce(Dummy.class, -20);
        assertTrue(dummies.isEmpty());
    }

    @Test
    public void noZeroConstructorErrorList() {
        final IProduceFactory dummyGenPopulateFactory = new GenProduceFactory();
        final List<DummyNoZeroConstructor> dummies = dummyGenPopulateFactory.produce(DummyNoZeroConstructor.class, 20);
        assertTrue(dummies.isEmpty());
    }

    @Test
    public void noZeroConstructorError() {
        final IProduceFactory dummyGenPopulateFactory = new GenProduceFactory();
        final DummyNoZeroConstructor dummy = dummyGenPopulateFactory.produce(DummyNoZeroConstructor.class);
        assertNull(dummy);
    }

    @Test
    public void produceListOfTwo() {
        final IProduceFactory dummyGenPopulateFactory = new GenProduceFactory();
        final List<Dummy> dummies = dummyGenPopulateFactory.produce(Dummy.class, 2);

        assertNotNull(dummies);
        assertFalse(dummies.isEmpty());

        final Dummy dummy1 = dummies.get(0);
        assertNotNull(dummy1);
        assertNotNull(dummy1.getCity());
        assertNotNull(dummy1.getName());
        assertNotNull(dummy1.getNum());
        assertNotNull(dummy1.getGroup());

        final Dummy dummy2 = dummies.get(1);
        assertNotNull(dummy2);
        assertNotNull(dummy2.getCity());
        assertNotNull(dummy2.getName());
        assertNotNull(dummy2.getNum());
        assertNotNull(dummy2.getGroup());
    }

    @Test
    public void produceSingleDummy() {
        final IProduceFactory dummyGenPopulateFactory = new GenProduceFactory();
        final Dummy dummy = dummyGenPopulateFactory.produce(Dummy.class);

        assertNotNull(dummy);
        assertNotNull(dummy.getCity());
        assertNotNull(dummy.getName());
        assertNotNull(dummy.getNum());
        assertNotNull(dummy.getGroup());

        assertTrue(dummy.getCity().matches("[a-zA-Z0-9]+"));
        assertTrue(dummy.getName().matches("[a-zA-Z]+"));
        assertEquals((int) dummy.getNum(), 0);
        assertEquals("100", dummy.getGroup());
    }

    @Test
    public void produceWithNoPopulateFields() {
        final IProduceFactory dummyGenPopulateFactory = new GenProduceFactory();
        final DummyNoPopulateFields dummy = dummyGenPopulateFactory.produce(DummyNoPopulateFields.class);

        assertNotNull(dummy);
        assertNull(dummy.getCity());
        assertNull(dummy.getName());
        assertNull(dummy.getNum());
        assertEquals("100", dummy.getGroup());
    }

    @Test
    public void produceWithCollectionFields() {
        final IProduceFactory dummyGenPopulateFactory = new GenProduceFactory();
        final DummyCollection dummy = dummyGenPopulateFactory.produce(DummyCollection.class);

        assertNotNull(dummy);
        assertNotNull(dummy.getObjects());
        assertNotNull(dummy.getStrings());
        assertNotNull(dummy.getMap());

        assertNotNull(dummy.getObjectsFix());
        assertNotNull(dummy.getStringsFix());
        assertNotNull(dummy.getMapFix());

        assertFalse(dummy.getObjects().isEmpty());
        assertFalse(dummy.getStrings().isEmpty());
        assertFalse(dummy.getMap().isEmpty());

        assertFalse(dummy.getObjectsFix().isEmpty());
        assertFalse(dummy.getStringsFix().isEmpty());
        assertFalse(dummy.getMapFix().isEmpty());
        assertEquals(dummy.getObjectsFix().size(), 4);
        assertEquals(dummy.getStringsFix().size(), 5);
        assertEquals(dummy.getMapFix().size(), 3);
    }

    @Test
    public void produceWithWrongCollectionFields() {
        final IProduceFactory dummyGenPopulateFactory = new GenProduceFactory();
        final DummyCollectionWrong dummy = dummyGenPopulateFactory.produce(DummyCollectionWrong.class);

        assertNotNull(dummy);

        assertNull(dummy.getList());
        assertNull(dummy.getSet());
        assertNull(dummy.getMap());
    }

    @Test
    public void produceWithTimeFields() {
        final IProduceFactory dummyGenPopulateFactory = new GenProduceFactory();
        final DummyTime dummy = dummyGenPopulateFactory.produce(DummyTime.class);

        assertNotNull(dummy);
        assertNotNull(dummy.getDateOld());
        assertNotNull(dummy.getDate());
        assertNotNull(dummy.getTime());
        assertNotNull(dummy.getDateTime());
        assertNotNull(dummy.getTimestamp());
        assertNotNull(dummy.getDateTimeString());
        assertNotNull(dummy.getDateTimeObject());

        final Pattern datePattern = Pattern.compile("[A-Za-z]{3} [A-Za-z]{3} \\d{2} \\d{2}:\\d{2}:\\d{2} [A-Za-z]{3} \\d{4}");
        final Pattern localDatePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        final Pattern localDateTimePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}[A-Z]\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?");
        final Pattern localTimePattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,10})?");
        final Pattern timestampPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d{1,10})?");

        assertTrue(datePattern.matcher(dummy.getDateOld().toString()).matches());
        assertTrue(localDatePattern.matcher(dummy.getDate().toString()).matches());
        assertTrue(localTimePattern.matcher(dummy.getTime().toString()).matches());
        assertTrue(localDateTimePattern.matcher(dummy.getDateTime().toString()).matches());
        assertTrue(timestampPattern.matcher(dummy.getTimestamp().toString()).matches());
        assertTrue(localDateTimePattern.matcher(dummy.getDateTimeObject().toString()).matches());
        assertTrue(localDateTimePattern.matcher(dummy.getDateTimeString()).matches());
    }
}
