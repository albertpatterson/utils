package stream.exception;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ExceptionalTest {

    @Test
    public void anyExceptionFalseFiniteStream() {
        final IntStream intStream = createFiniteStream();

        final Stream<Exceptional<Integer>> exceptionalStream= intStream.boxed().map(Exceptional::new);
        final Exceptional<Stream<Integer>> streamExceptional = Exceptional.anyException(exceptionalStream);

        assertFalse(streamExceptional.hasException());

        Integer[] actualExceptionalValues = null;
        if(streamExceptional.getValue() != null) actualExceptionalValues = streamExceptional.getValue().toArray(Integer[]::new);

        Integer[] expectedExceptionalValues = createFiniteStream().boxed().toArray(Integer[]::new);

        assertArrayEquals(expectedExceptionalValues, actualExceptionalValues);

        assertEquals(streamExceptional.getException(), null);
    }

    @Test
    public void anyExceptionTrueFiniteStream() {
        final IntStream intStream = createFiniteStream();
        final Exception testException = new Exception("Testing: value = 5");
        final int errorVal = 5;

        final Stream<Exceptional<Integer>> exceptionalStream= intStream.boxed().map(i->{
            if(i==errorVal){
                return new Exceptional<Integer>(testException);
            }else{
                return new Exceptional<Integer>(i);
            }
        });
        final Exceptional<Stream<Integer>> streamExceptional = Exceptional.anyException(exceptionalStream);

        assertTrue(streamExceptional.hasException());

        Stream<Integer> actualExceptionalValues = streamExceptional.getValue();

        Stream<Integer> expectedExceptionalValues = null;

        assertEquals(expectedExceptionalValues, actualExceptionalValues);

        assertEquals(testException, streamExceptional.getException());
    }

    @Test
    public void anyExceptionTrueInfiniteStream() {
        final IntStream intStream = createInfiniteStream();
        final Exception testException = new Exception("Testing: value = 5");
        final int errorVal = 5;

        final List<Integer> processedValuesList = new ArrayList<>();
        final Stream<Exceptional<Integer>> exceptionalStream= intStream.boxed().map(i->{
            processedValuesList.add(i);
            if(i==errorVal){
                return new Exceptional<Integer>(testException);
            }else{
                return new Exceptional<Integer>(i);
            }
        });
        final Exceptional<Stream<Integer>> streamExceptional = Exceptional.anyException(exceptionalStream);

        assertTrue(streamExceptional.hasException());

        Stream<Integer> actualExceptionalValues = streamExceptional.getValue();

        Stream<Integer> expectedExceptionalValues = null;

        assertEquals(expectedExceptionalValues, actualExceptionalValues);

        assertEquals(testException, streamExceptional.getException());

        Integer[] expectedProcessedValues = {0, 1, 2, 3, 4, 5};

        Integer[] actualProcessValues = processedValuesList.toArray(new Integer[5]);

        assertArrayEquals(expectedProcessedValues, actualProcessValues);
    }

    @Test
    public void hasExceptionFalse() {
        final Integer testValue = 0;
        Exceptional<Integer> exception = new Exceptional<>(testValue);
        assertFalse(exception.hasException());
        assertEquals(exception.getValue(), testValue);
        assertEquals(exception.getException(), null);
    }

    @Test
    public void hasExceptionTrue() {
        final Exception testException = new Exception("testing Exceptional");
        Exceptional<Integer> exception = new Exceptional<>(testException);
        assertTrue(exception.hasException());
        assertEquals(exception.getValue(), null);
        assertEquals(exception.getException(), testException);
    }

    private IntStream createFiniteStream(){
        return IntStream.range(0, 50);
    }

    private IntStream createInfiniteStream() {
        return IntStream.iterate(0, i->i+1);
    }
}