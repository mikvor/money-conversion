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

import java.util.Random;

/**
 * All arithmetic operations
 *
 *
 * long+long - too much conditional logic? (compare to long*long)
 * long*double - should be faster (even after added shortcut - result with same precision)
 Time to add 10000000 MoneyLong values = 0.335 sec; rate = 29850 Kops/sec
 Time to add 10000000 MoneyLong to MoneyBigDecimal values = 11.94 sec; rate = 837 Kops/sec
 Time to add 10000000 MoneyBigDecimal to MoneyBigDecimal values = 16.641 sec; rate = 600 Kops/sec
 Time to multiply 10000000 MoneyLong values by Long = 0.171 sec; rate = 58479 Kops/sec
 Time to multiply 10000000 MoneyLong values by Double = 1.039 sec; rate = 9624 Kops/sec
 Time to multiply 10000000 MoneyBigDecimal values by Long = 12.489 sec; rate = 800 Kops/sec
 Time to multiply 10000000 MoneyBigDecimal values by Double = 18.761 sec; rate = 533 Kops/sec
 Time to divide 10000000 MoneyLong values by Long = 0.67 sec; rate = 14925 Kops/sec
 Time to divide 10000000 MoneyLong values by Double = 0.593 sec; rate = 16863 Kops/sec
 Time to divide 10000000 MoneyBigDecimal values by Long = 16.86 sec; rate = 593 Kops/sec
 Time to divide 10000000 MoneyBigDecimal values by Double = 22.259 sec; rate = 449 Kops/sec
 Time to truncate 10000000 MoneyLong values  = 0.533 sec; rate = 18761 Kops/sec
 Time to truncate 10000000 MoneyBigDecimal values  = 7.091 sec; rate = 1410 Kops/sec


 Time to add 10000000 MoneyLong values = 0.335 sec; rate = 29850 Kops/sec
 Time to multiply 10000000 MoneyLong values by Long = 0.171 sec; rate = 58479 Kops/sec
 Time to multiply 10000000 MoneyLong values by Double = 1.039 sec; rate = 9624 Kops/sec
 Time to divide 10000000 MoneyLong values by Long = 0.67 sec; rate = 14925 Kops/sec
 Time to divide 10000000 MoneyLong values by Double = 0.593 sec; rate = 16863 Kops/sec
 Time to truncate 10000000 MoneyLong values = 0.533 sec; rate = 18761 Kops/sec

 ----------------
Battery:

 Time to add 10000000 MoneyLong values = 0.398 sec; rate = 25125 Kops/sec
 Time to multiply 10000000 MoneyLong values by Long = 0.146 sec; rate = 68493 Kops/sec
 Time to multiply 10000000 MoneyLong values by Double = 0.689 sec; rate = 14513 Kops/sec
 Time to divide 10000000 MoneyLong values by Long = 0.654 sec; rate = 15290 Kops/sec
 Time to divide 10000000 MoneyLong values by Double = 0.578 sec; rate = 17301 Kops/sec
 Time to truncate 10000000 MoneyLong values  = 0.513 sec; rate = 19493 Kops/sec


 */
public class OperationsTests {
    private static final int DATA_SIZE = 1000;
    private static final int WARMUP = 20000;
    private static final int ACTUAL_TEST = 10 * 1000 * 1000;


    public static void main(String[] args) {
        new OperationsTests( DATA_SIZE ).runAllTests( WARMUP, ACTUAL_TEST );
    }

    private final Money[] dataLong;
    private final Money[] dataBd;

    public OperationsTests( final int dataSize )
    {
        final Random r = new Random( 123 );
        dataLong = new Money[ dataSize ];
        for ( int i = 0; i < dataLong.length; ++i )
            dataLong[ i ] = MoneyFactory.fromUnits(r.nextInt(100000), 2);

        dataBd = new Money[ dataSize ];
        for ( int i = 0; i < dataBd.length; ++i )
            dataBd[ i ] = MoneyFactory.fromDouble((i + 1) / 10000.0, 1);
    }

    public void runAllTests( final int warmup, final int iters )
    {
        testLongAddition( warmup );
        testLongAddition( iters );
        testLongAddition( iters );

//        testLongToBigDecimalAddition( warmup );
//        testLongToBigDecimalAddition( iters );
//
//        testBdToBdAddition( warmup );
//        testBdToBdAddition( iters );

        testLongMultiplyLong(warmup);
        testLongMultiplyLong(iters);
        testLongMultiplyLong(iters);

        testLongMultiplyDouble(warmup);
        testLongMultiplyDouble(iters);
        testLongMultiplyDouble(iters);

//        testBdMultiplyLong( warmup );
//        testBdMultiplyLong(iters);
//
//        testBdMultiplyDouble( warmup );
//        testBdMultiplyDouble( iters );

        testLongDivideLong(warmup);
        testLongDivideLong(iters);
        testLongDivideLong(iters);

        testLongDivideDouble(warmup);
        testLongDivideDouble(iters);
        testLongDivideDouble(iters);

//        testBdDivideLong(warmup);
//        testBdDivideLong(iters);
//
//        testBdDivideDouble(warmup);
//        testBdDivideDouble(iters);

        testLongTruncate( warmup );
        testLongTruncate( iters );
        testLongTruncate( iters );

//        testBdTruncate( warmup );
//        testBdTruncate( iters );
    }

    public long testLongAddition( final int iters )
    {
        final int actualIters = iters / dataLong.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            Money sum = dataLong[ 0 ];
            for ( int j = 1; j < dataLong.length; ++j )
                sum = sum.add( dataLong[ j ] );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to add " + iters + " MoneyLong values = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testLongToBigDecimalAddition( final int iters )
    {
        final Money bd = MoneyFactory.fromDouble( 0.555, 1 );

        final int actualIters = iters / dataLong.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataLong.length; ++j )
                dataLong[ j ].add( bd );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to add " + iters + " MoneyLong to MoneyBigDecimal values = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testBdToBdAddition( final int iters )
    {
        final Money bd = MoneyFactory.fromDouble( 0.555, 1 );

        final int actualIters = iters / dataBd.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataBd.length; ++j )
                bd.add( dataBd[ j ] );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to add " + iters + " MoneyBigDecimal to MoneyBigDecimal values = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testLongMultiplyLong( final int iters )
    {
        final int actualIters = iters / dataLong.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataLong.length; ++j )
                dataLong[ j ].multiply( 97 );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to multiply " + iters + " MoneyLong values by Long = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testLongMultiplyDouble( final int iters )
    {
        final int actualIters = iters / dataLong.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataLong.length; ++j )
                dataLong[ j ].multiply( 97.5 );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to multiply " + iters + " MoneyLong values by Double = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testBdMultiplyLong( final int iters )
    {
        final int actualIters = iters / dataBd.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataBd.length; ++j )
                dataBd[ j ].multiply( 97 );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to multiply " + iters + " MoneyBigDecimal values by Long = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testBdMultiplyDouble( final int iters )
    {
        final int actualIters = iters / dataBd.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataBd.length; ++j )
                dataBd[ j ].multiply( 97.5 );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to multiply " + iters + " MoneyBigDecimal values by Double = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testLongDivideLong( final int iters )
    {
        final int actualIters = iters / dataLong.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataLong.length; ++j )
                dataLong[ j ].divide( 97, 4 );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to divide " + iters + " MoneyLong values by Long = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testLongDivideDouble(final int iters)
    {
        final int actualIters = iters / dataLong.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataLong.length; ++j )
                dataLong[ j ].divide( 97.5, 4 );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to divide " + iters + " MoneyLong values by Double = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testBdDivideLong(final int iters)
    {
        final int actualIters = iters / dataBd.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataBd.length; ++j )
                dataBd[ j ].divide( 97, 4 );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to divide " + iters + " MoneyBigDecimal values by Long = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testBdDivideDouble(final int iters)
    {
        final int actualIters = iters / dataBd.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataBd.length; ++j )
                dataBd[ j ].divide( 97.5, 4 );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to divide " + iters + " MoneyBigDecimal values by Double = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testLongTruncate( final int iters )
    {
        final int actualIters = iters / dataLong.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataLong.length; ++j )
                dataLong[ j ].truncate( 1 );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to truncate " + iters + " MoneyLong values  = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

    public long testBdTruncate( final int iters )
    {
        final int actualIters = iters / dataBd.length;
        final long start = System.currentTimeMillis();
        for ( int i = 0; i < actualIters; ++i )
        {
            for ( int j = 0; j < dataBd.length; ++j )
                dataBd[ j ].truncate( 1 );
        }
        final long time = System.currentTimeMillis() - start;
        final long rate = (long) (1.0 * iters / ( time / 1000.0 )) / 1000;
        if ( iters > 20000 )
            System.out.println( "Time to truncate " + iters + " MoneyBigDecimal values  = " + time / 1000.0 + " sec; rate = " + rate + " Kops/sec");
        return rate;
    }

}
