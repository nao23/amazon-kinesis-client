/*
 * Copyright 2023 Amazon.com, Inc. or its affiliates.
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.amazon.kinesis.common;

import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SynchronizedCacheTest {

    private static final Object DUMMY_RESULT = SynchronizedCacheTest.class;

    @Mock
    private Supplier<Object> mockSupplier;

    private final SynchronizedCache<Object> cache = new SynchronizedCache<>();

    @Test
    public void testCache() {
        when(mockSupplier.get()).thenReturn(DUMMY_RESULT);

        final Object result1 = cache.get(mockSupplier);
        final Object result2 = cache.get(mockSupplier);

        assertEquals(DUMMY_RESULT, result1);
        assertSame(result1, result2);
        verify(mockSupplier).get();
    }

    @Test
    public void testCacheWithNullResult() {
        when(mockSupplier.get()).thenReturn(null).thenReturn(DUMMY_RESULT);

        assertNull(cache.get(mockSupplier));
        assertEquals(DUMMY_RESULT, cache.get(mockSupplier));
        assertEquals(DUMMY_RESULT, cache.get(mockSupplier));
        verify(mockSupplier, times(2)).get();
    }
}