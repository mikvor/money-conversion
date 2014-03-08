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

package info.javaperformance.money.performance;

import info.javaperformance.money.MoneyFactory;

import java.math.BigDecimal;

/**
 * Testing conversion from various data types into Money performance
 */
public class FromConversionTests {
    public static void main(String[] args) {
        new FromConversionTests().runAllTests( 20000, 10 * 1000 * 1000 );
    }
    
    public void runAllTests( final int warmup, final int iters )
    {
        testFromDoubleConversion( warmup );
        testFromDoubleConversion( iters );

        testFromStringConversion1Digit( warmup );
        testFromStringConversion1Digit( iters );

        testFromStringConversion4Digits( warmup );
        testFromStringConversion4Digits( iters );

        testFromBigDecimalConversionIn3DigitsRange( warmup );
        testFromBigDecimalConversionIn3DigitsRange( iters );

        testFromBigDecimalConversionOutOf3DigitsRange( warmup );
        testFromBigDecimalConversionOutOf3DigitsRange( iters );
    }

    public long testFromDoubleConversion( final int iters )
    {
        //build precise source data
        final int DATA_SIZE = 1000;
        final double[] src = new double[ DATA_SIZE ];
        for ( int i = 0; i < DATA_SIZE; ++i )
            src[ i ] = i / 10.0;

        //now the test itself
        final int actualIters = iters / DATA_SIZE;
        int cnt = 0;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < DATA_SIZE; ++j )
            {
                if ( MoneyFactory.fromDouble( src[ j ], 1 ) != null )
                    cnt++;
            }
        }
        final long time = System.currentTimeMillis() - start;

        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to convert from double " + iters + " times = " + ( time/1000.0 ) + " sec, rate = " + rate + " Kop/sec, cnt = " + cnt );
        return rate;
    }

    public long testFromStringConversion1Digit(final int iters)
    {
        //build source data
        final int DATA_SIZE = 1000;
        final String[] src = new String[ DATA_SIZE ];
        for ( int i = 0; i < DATA_SIZE; ++i )
            src[ i ] = Integer.toString( i / 10 ) + "." + Integer.toString( i % 10 );

        //now the test itself
        final int actualIters = iters / DATA_SIZE;
        int cnt = 0;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < DATA_SIZE; ++j )
            {
                if ( MoneyFactory.fromString( src[ j ] ) != null )
                    cnt++;
            }
        }
        final long time = System.currentTimeMillis() - start;

        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to convert from string (1 decimal digit) " + iters + " times = " + ( time/1000.0 ) + " sec, rate = " + rate + " Kop/sec, cnt = " + cnt );
        return rate;
    }

    private static String four( final int val )
    {
        String res = Integer.toString( val );
        while ( res.length() < 4 )
            res = "0" + res;
        return res;
    }

    public long testFromStringConversion4Digits( final int iters )
    {
        //build source data
        final int DATA_SIZE = 1000;
        final String[] src = new String[ DATA_SIZE ];
        for ( int i = 0; i < DATA_SIZE; ++i )
            src[ i ] = "0." + four(i);

        //now the test itself
        final int actualIters = iters / DATA_SIZE;
        int cnt = 0;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < DATA_SIZE; ++j )
            {
                if ( MoneyFactory.fromString( src[ j ] ) != null )
                    cnt++;
            }
        }
        final long time = System.currentTimeMillis() - start;

        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to convert from string (4 decimal digit) " + iters + " times = " + ( time/1000.0 ) + " sec, rate = " + rate + " Kop/sec, cnt = " + cnt );
        return rate;
    }

    public long testFromBigDecimalConversionIn3DigitsRange(final int iters)
    {
        //build source data
        final int DATA_SIZE = 1000;
        final BigDecimal[] src = new BigDecimal[ DATA_SIZE ];
        BigDecimal cur = BigDecimal.ZERO;
        final BigDecimal incr = new BigDecimal( "0.1" );
        for ( int i = 0; i < DATA_SIZE; ++i )
        {
            src[ i ] = cur;
            cur = cur.add( incr );
        }

        //now the test itself
        final int actualIters = iters / DATA_SIZE;
        int cnt = 0;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < DATA_SIZE; ++j )
            {
                if ( MoneyFactory.fromBigDecimal(src[j])  != null )
                    cnt++;
            }
        }
        final long time = System.currentTimeMillis() - start;

        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to convert from BigDecimal (1 decimal digit) " + iters + " times = " + ( time/1000.0 ) + " sec, rate = " + rate + " Kop/sec, cnt = " + cnt );
        return rate;
    }

    public long testFromBigDecimalConversionOutOf3DigitsRange(final int iters)
    {
        //build source data
        final int DATA_SIZE = 1000;
        final BigDecimal[] src = new BigDecimal[ DATA_SIZE ];
        BigDecimal cur = BigDecimal.ZERO;
        final BigDecimal incr = new BigDecimal( "0.0001" );
        for ( int i = 0; i < DATA_SIZE; ++i )
        {
            src[ i ] = cur;
            cur = cur.add( incr );
        }

        //now the test itself
        final int actualIters = iters / DATA_SIZE;
        int cnt = 0;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < DATA_SIZE; ++j )
            {
                if ( MoneyFactory.fromBigDecimal(src[j])  != null )
                    cnt++;
            }
        }
        final long time = System.currentTimeMillis() - start;

        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to convert from BigDecimal (4 decimal digits) " + iters + " times = " + ( time/1000.0 ) + " sec, rate = " + rate + " Kop/sec, cnt = " + cnt );
        return rate;
    }
}
