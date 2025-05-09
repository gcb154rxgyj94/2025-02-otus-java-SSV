package ru.otus.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorGenerateException;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class TestProcessorGenerateException {

    private Message message;

    @BeforeEach
    public void setUp() {
        message = new Message.Builder(1L).field7("field7").build();
    }

    @Test
    public void testEvenSeconds() {
        Processor processor = new ProcessorGenerateException(() -> LocalTime.of(0, 0,2));
        ComplexProcessor complexProcessor = new ComplexProcessor(List.of(processor), e -> {
            throw new TestException(e.getMessage());
        });
        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.handle(message));
    }

    @Test
    public void testOddSeconds() {
        Processor processor = new ProcessorGenerateException(() -> LocalTime.of(0, 0,1));
        ComplexProcessor complexProcessor = new ComplexProcessor(List.of(processor), e -> {
            throw new TestException(e.getMessage());
        });
        assertThatNoException().isThrownBy(() -> complexProcessor.handle(message));
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }

}
