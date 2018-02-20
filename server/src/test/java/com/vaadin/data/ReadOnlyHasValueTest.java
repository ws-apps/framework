package com.vaadin.data;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Label;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class ReadOnlyHasValueTest {
    public static final String SAY_SOMETHING = "Say something";
    private static final String SAY_SOMETHING_ELSE = "Say something else";
    private Label label;
    private ReadOnlyHasValue<String> hasValue;

    @Before
    public void setup() {
        label = new Label();
        hasValue = new ReadOnlyHasValue<>(label::setCaption);
    }

    @Test
    public void testBase() {
        hasValue.setReadOnly(true);
        hasValue.setRequiredIndicatorVisible(false);
        Registration registration = hasValue.addValueChangeListener(e -> {
        });
        registration.remove();
        hasValue.setValue(SAY_SOMETHING);
        assertEquals(SAY_SOMETHING, hasValue.getValue());
        assertEquals(SAY_SOMETHING, label.getCaption());
        hasValue.setValue(SAY_SOMETHING_ELSE);
        assertEquals(SAY_SOMETHING_ELSE, hasValue.getValue());
        assertEquals(SAY_SOMETHING_ELSE, label.getCaption());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRO() {
        hasValue.setReadOnly(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndicator() {
        hasValue.setRequiredIndicatorVisible(true);
    }

    @Test
    public void testBind() {
        Binder<Bean> beanBinder = new Binder<>(Bean.class);
        Label label = new Label();
        ReadOnlyHasValue<Long> intHasValue = new ReadOnlyHasValue<>(
                i -> label.setValue(Objects.toString(i, "")));
        beanBinder.forField(intHasValue).bind("v");

        beanBinder.readBean(new Bean(42));
        assertEquals("42", label.getValue());
        assertEquals(42L, intHasValue.getValue().longValue());

        beanBinder.readBean(new Bean(1984));
        assertEquals("1984", label.getValue());
        assertEquals(1984L, intHasValue.getValue().longValue());

        beanBinder.readBean(new Bean(1984));
        assertEquals("1984", label.getValue());
        assertEquals(1984L, intHasValue.getValue().longValue());

        beanBinder.readBean(null);
        assertEquals("", label.getValue());
        assertEquals(null, intHasValue.getValue());

    }

    public static class Bean {
        public Bean(long v) {
            this.v = v;
        }

        private long v;

        public long getV() {
            return v;
        }

        public void setV(long v) {
            this.v = v;
        }
    }
}
