/*
* Copyright 2014 Mikhail Vorontsov
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package info.javaperformance.money;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class LongBitSet
{
    /** Number of bits allocated to a value in an index */
    private static final int VALUE_BITS = 20; //1M values per bit set
    /** Mask for extracting values */
    private static final long VALUE_MASK = ( 1 << VALUE_BITS ) - 1;

    /**
     * Map from a value stored in high bits of a long index to a bit set mapped to the lower bits of an index.
     * Bit sets size should be balanced - not to long (otherwise setting a single bit may waste megabytes of memory)
     * but not too short (otherwise this map will get too big). Update value of {@code VALUE_BITS} for your needs.
     * In most cases it is ok to keep 1M - 64M values in a bit set, so each bit set will occupy 128Kb - 8Mb.
     */
    private Map<Long, BitSet> m_sets = new HashMap<Long, BitSet>( 20 );

    /**
     * Get set index by long index (extract bits 20-63)
     * @param index Long index
     * @return Index of a bit set in the inner map
     */
    private long getSetIndex( final long index )
    {
        return index >> VALUE_BITS;
    }

    /**
     * Get index of a value in a bit set (bits 0-19)
     * @param index Long index
     * @return Index of a value in a bit set
     */
    private int getPos( final long index )
    {
        return (int) (index & VALUE_MASK);
    }

    /**
     * Helper method to get (or create, if necessary) a bit set for a given long index
     * @param index Long index
     * @return A bit set for a given index (always not null)
     */
    private BitSet bitSet( final long index )
    {
        final Long iIndex = getSetIndex( index );
        BitSet bitSet = m_sets.get( iIndex );
        if ( bitSet == null )
        {
            bitSet = new BitSet( 1024 );
            m_sets.put( iIndex, bitSet );
        }
        return bitSet;
    }

    /**
     * Set a given value for a given index
     * @param index Long index
     * @param value Value to set
     */
    public void set( final long index, final boolean value )
    {
        if ( value )
            bitSet( index ).set( getPos( index ), value );
        else
        {  //if value shall be cleared, check first if given partition exists
            final BitSet bitSet = m_sets.get( getSetIndex( index ) );
            if ( bitSet != null )
                bitSet.clear( getPos( index ) );
        }
    }

    /**
     * Get a value for a given index
     * @param index Long index
     * @return Value associated with a given index
     */
    public boolean get( final long index )
    {
        final BitSet bitSet = m_sets.get( getSetIndex( index ) );
        return bitSet != null && bitSet.get( getPos( index ) );
    }

    /**
     * Clear all bits between {@code fromIndex} (inclusive) and {@code toIndex} (exclusive)
     * @param fromIndex Start index (inclusive)
     * @param toIndex End index (exclusive)
     */
    public void clear( final long fromIndex, final long toIndex )
    {
        if ( fromIndex >= toIndex ) return;
        final long fromPos = getSetIndex( fromIndex );
        final long toPos = getSetIndex( toIndex );
        //remove all maps in the middle
        for ( long i = fromPos + 1; i < toPos; ++i )
            m_sets.remove( i );
        //clean two corner sets manually
        final BitSet fromSet = m_sets.get( fromPos );
        final BitSet toSet = m_sets.get( toPos );
        ///are both ends in the same subset?
        if ( fromSet != null && fromSet == toSet )
        {
            fromSet.clear( getPos( fromIndex ), getPos( toIndex ) );
            return;
        }
        //clean left subset from left index to the end
        if ( fromSet != null )
            fromSet.clear( getPos( fromIndex ), fromSet.length() );
        //clean right subset from 0 to given index. Note that both checks are independent
        if ( toSet != null )
            toSet.clear( 0, getPos( toIndex ) );
    }

//    /**
//     * Iteration over all set values in a LongBitSet. Order of iteration is not specified.
//     * @param proc Procedure to call. If it returns {@code false}, then iteration will stop at once
//     */
//    public void forEach( final LongProcedure proc )
//    {
//        for ( final Map.Entry<Long, BitSet> entry : m_sets.entrySet() )
//        {
//            final BitSet bs = entry.getValue();
//            final long baseIndex = entry.getKey() << VALUE_BITS;
//            for ( int i = bs.nextSetBit( 0 ); i >= 0; i = bs.nextSetBit( i + 1 ) ) {
//                if ( !proc.forEntry( baseIndex + i ) )
//                    return;
//            }
//        }
//    }
}