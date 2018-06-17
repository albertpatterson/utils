package stream.exception;

import java.util.stream.Stream;

public class Exceptional<T> {

    public static class ExceptionTracker<U> {
        private Exception exception;

        void trackLast(Exceptional<U> Exceptional) {
            if (Exceptional.hasException()) exception = Exceptional.getException();
        }

        Exception getLast() {
            return exception;
        }
    }

    public static <U> Exceptional<Stream<U>> anyException(Stream<Exceptional<U>> stream) {
        Stream.Builder<Exceptional<U>> eStreamBuider = Stream.builder();
        ExceptionTracker<U> exceptionTracker = new ExceptionTracker<>();
        boolean anyException = stream
                .peek(eStreamBuider)
                .peek(exceptionTracker::trackLast)
                .anyMatch(Exceptional::hasException);

        Stream<Exceptional<U>> eStream = eStreamBuider.build();

        if (anyException) {
            return new Exceptional<Stream<U>>(exceptionTracker.getLast());
        } else {
            return new Exceptional<Stream<U>>(eStream.map(Exceptional::getValue));
        }
    }


    private Exception exception;
    private T value;

    public Exceptional(Exception _exception) {
        exception = _exception;
    }

    public Exceptional(T _value) {
        value = _value;
    }

    public Boolean hasException() {
        return exception != null;
    }

    public T getValue() {
        return value;
    }

    public Exception getException() {
        return exception;
    }
}

