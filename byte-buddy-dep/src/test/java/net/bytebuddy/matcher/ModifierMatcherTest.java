package net.bytebuddy.matcher;

import net.bytebuddy.instrumentation.ModifierReviewable;
import net.bytebuddy.utility.ObjectPropertyAssertion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class ModifierMatcherTest extends AbstractElementMatcherTest<ModifierMatcher<?>> {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ModifierMatcher.Mode.BRIDGE, Opcodes.ACC_BRIDGE},
                {ModifierMatcher.Mode.FINAL, Opcodes.ACC_FINAL},
                {ModifierMatcher.Mode.NATIVE, Opcodes.ACC_NATIVE},
                {ModifierMatcher.Mode.PRIVATE, Opcodes.ACC_PRIVATE},
                {ModifierMatcher.Mode.PROTECTED, Opcodes.ACC_PROTECTED},
                {ModifierMatcher.Mode.PUBLIC, Opcodes.ACC_PUBLIC},
                {ModifierMatcher.Mode.STATIC, Opcodes.ACC_STATIC},
                {ModifierMatcher.Mode.STRICT, Opcodes.ACC_STRICT},
                {ModifierMatcher.Mode.SYNCHRONIZED, Opcodes.ACC_SYNCHRONIZED},
                {ModifierMatcher.Mode.SYNTHETIC, Opcodes.ACC_SYNTHETIC}
        });
    }

    private final ModifierMatcher.Mode mode;

    private final int modifiers;

    @SuppressWarnings("unchecked")
    public ModifierMatcherTest(ModifierMatcher.Mode mode, int modifiers) {
        super((Class<ModifierMatcher<?>>) (Object) ModifierMatcher.class, mode.getDescription());
        this.mode = mode;
        this.modifiers = modifiers;
    }

    @Mock
    private ModifierReviewable modifierReviewable;

    @Test
    public void testMatch() throws Exception {
        when(modifierReviewable.getModifiers()).thenReturn(modifiers);
        assertThat(new ModifierMatcher<ModifierReviewable>(mode).matches(modifierReviewable), is(true));
        verify(modifierReviewable).getModifiers();
        verifyNoMoreInteractions(modifierReviewable);
    }

    @Test
    public void testNoMatch() throws Exception {
        when(modifierReviewable.getModifiers()).thenReturn(0);
        assertThat(new ModifierMatcher<ModifierReviewable>(mode).matches(modifierReviewable), is(false));
        verify(modifierReviewable).getModifiers();
        verifyNoMoreInteractions(modifierReviewable);
    }

    @Override
    protected <S> ObjectPropertyAssertion<S> modify(ObjectPropertyAssertion<S> propertyAssertion) {
        return propertyAssertion.skipToString();
    }

    @Test
    public void testToString() throws Exception {
        assertThat(new ModifierMatcher<ModifierReviewable>(mode).toString(), is(mode.getDescription()));
    }
}
