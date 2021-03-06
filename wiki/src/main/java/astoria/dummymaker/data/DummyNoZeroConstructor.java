package astoria.dummymaker.data;

import io.dummymaker.annotation.collection.GenList;
import io.dummymaker.annotation.collection.GenMap;
import io.dummymaker.annotation.collection.GenSet;
import io.dummymaker.generator.impl.EmbeddedGenerator;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * "default comment"
 *
 * @author GoodforGod
 * @since 27.02.2018
 */
public class DummyNoZeroConstructor {

    private final Integer amount;

    private Dummy dummy;

    @GenMap(key = EmbeddedGenerator.class, value = EmbeddedGenerator.class)
    private Map<String, Object> map;

    @GenList(value = EmbeddedGenerator.class, fixed = 4)
    private List<String> objectsFix;

    @GenSet(fixed = 5, value = EmbeddedGenerator.class)
    private Set<DummyNoZeroConstructor> stringsFix;

    public DummyNoZeroConstructor(int amount) {
        this.amount = amount;
    }
}
