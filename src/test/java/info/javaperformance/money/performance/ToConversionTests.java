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

import info.javaperformance.money.Money;
import info.javaperformance.money.MoneyFactory;

/**
 * Testing conversion to various types
 */
public class ToConversionTests {
    public static void main(String[] args) {
        new ToConversionTests().runAllTests( 20000, 10 * 1000 * 1000 );
    }

    public void runAllTests( final int warmup, final int iters )
    {
        testLongToDouble( warmup );
        testLongToDouble( iters * 10 );

        //todo performance could be improved here
        testLongToString( warmup );
        testLongToString( iters );

        testLongToBigDecimal( warmup );
        testLongToBigDecimal( iters );

        testBigDecimalToDouble( warmup );
        testBigDecimalToDouble( iters );

        testBigDecimalToString( warmup );
        testBigDecimalToString( iters );
    }

    private long testLongToDouble( final int iters )
    {
        //prepare the data set
        final int DATA_SIZE = 1000;
        final Money[] money = new Money[ DATA_SIZE ];
        for ( int i = 0; i < DATA_SIZE; ++i )
            money[ i ] = MoneyFactory.fromUnits( i, 2 );
        //test
        final int actualLoops = iters / DATA_SIZE;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualLoops; ++i )
        {
            for ( int j = 0; j < DATA_SIZE; ++j )
            {
                money[ j ].toDouble();
            }
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (( 1.0 * iters / ( time / 1000.0 ) )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to convert MoneyLong to double " + iters + " times = " + (time/1000.0) + " sec, rate = " + rate + " KOps/sec");
        return rate;
    }

    private long testLongToString( final int iters )
    {
        //prepare the data set
        final int DATA_SIZE = 1000;
        final Money[] money = new Money[ DATA_SIZE ];
        for ( int i = 0; i < DATA_SIZE; ++i )
            money[ i ] = MoneyFactory.fromUnits( i, 2 );
        //test
        final int actualLoops = iters / DATA_SIZE;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualLoops; ++i )
        {
            for ( int j = 0; j < DATA_SIZE; ++j )
            {
                money[ j ].toString();
            }
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (( 1.0 * iters / ( time / 1000.0 ) )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to convert MoneyLong to String " + iters + " times = " + (time/1000.0) + " sec, rate = " + rate + " KOps/sec");
        return rate;
    }

    private long testLongToBigDecimal( final int iters )
    {
        //prepare the data set
        final int DATA_SIZE = 1000;
        final Money[] money = new Money[ DATA_SIZE ];
        for ( int i = 0; i < DATA_SIZE; ++i )
            money[ i ] = MoneyFactory.fromUnits( i, 2 );
        //test
        final int actualLoops = iters / DATA_SIZE;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualLoops; ++i )
        {
            for ( int j = 0; j < DATA_SIZE; ++j )
            {
                money[ j ].toBigDecimal();
            }
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (( 1.0 * iters / ( time / 1000.0 ) )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to convert MoneyLong to BigDecimal " + iters + " times = " + (time/1000.0) + " sec, rate = " + rate + " KOps/sec");
        return rate;
    }

    private long testBigDecimalToDouble( final int iters )
    {
        //prepare the data set
        final int DATA_SIZE = 1000;
        final Money[] money = new Money[ DATA_SIZE ];
        for ( int i = 0; i < DATA_SIZE; ++i )
            money[ i ] = MoneyFactory.fromDouble( ( i + 1 ) / 1000.0, 1 );
        //test
        final int actualLoops = iters / DATA_SIZE;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualLoops; ++i )
        {
            for ( int j = 0; j < DATA_SIZE; ++j )
            {
                money[ j ].toDouble();
            }
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (( 1.0 * iters / ( time / 1000.0 ) )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to convert MoneyBigDecimal to double " + iters + " times = " + (time/1000.0) + " sec, rate = " + rate + " KOps/sec");
        return rate;
    }

    private long testBigDecimalToString( final int iters )
    {
        //prepare the data set
        final int DATA_SIZE = 1000;
        final Money[] money = new Money[ DATA_SIZE ];
        for ( int i = 0; i < DATA_SIZE; ++i )
            money[ i ] = MoneyFactory.fromDouble( ( i + 1 ) / 1000.0, 1 );
        //test
        final int actualLoops = iters / DATA_SIZE;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualLoops; ++i )
        {
            for ( int j = 0; j < DATA_SIZE; ++j )
            {
                money[ j ].toString();
            }
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (( 1.0 * iters / ( time / 1000.0 ) )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to convert MoneyBigDecimal to String " + iters + " times = " + (time/1000.0) + " sec, rate = " + rate + " KOps/sec");
        return rate;
    }

}
