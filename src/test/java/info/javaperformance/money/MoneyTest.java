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

import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.MathContext;

public class MoneyTest extends TestCase {
    public void testAdd()
    {
        //long and long
        final Money long02 = MoneyFactory.fromDouble( 0.2, 1 );
        final Money long03 = MoneyFactory.fromDouble( 0.3, 1 );
        final Money long05 = long02.add( long03 );
        assertEquals( 0.5, long05.toDouble() );
        //long and BD
        final Money bd005 = MoneyFactory.fromDouble( 0.05, 1 );
        final Money bd025 = bd005.add( long02 );
        assertEquals( 0.25, bd025.toDouble() );
        assertEquals( "0.25", bd025.toString() );
        //BD and BD
        final Money long01 = bd005.add( bd005 );
        assertTrue( long01 instanceof  MoneyLong );
        assertEquals(0.1, long01.toDouble());
        assertEquals( "0.1", long01.toString() );
    }

    public void testAdd2()
    {
        final Money bd0005 = MoneyFactory.fromDouble( 0.005, 2 );
        final Money bd9995 = MoneyFactory.fromDouble( 9.995, 2 );
        final Money long10 = bd0005.add( bd9995 );
        assertTrue( long10 instanceof MoneyLong );
        assertEquals(10.0, long10.toDouble());
        assertEquals( "10", long10.toString() );

        final Money long001 = bd0005.add( bd0005 );
        assertTrue( long001 instanceof MoneyLong );
        assertEquals(0.01, long001.toDouble());
        assertEquals( "0.01", long001.toString() );

        final Money long3567 = MoneyFactory.fromDouble( 35.67, 2 );
        assertEquals( "35.67", long3567.toString() );

        assertEquals( 100.0, MoneyFactory.fromDouble(50, 2).add(MoneyFactory.fromString("50")).toDouble());
        assertEquals( 100.5, MoneyFactory.fromDouble(50, 2).add(MoneyFactory.fromString("50.5")).toDouble());
        assertEquals( 100.5, MoneyFactory.fromDouble(50.5, 2).add(MoneyFactory.fromString("50")).toDouble());
    }

    public void testEqualsHashcode()
    {
        assertEquals( MoneyFactory.fromDouble(50.4, 2), MoneyFactory.fromUnits(504,1));
        assertEquals( MoneyFactory.fromDouble(50.4, 2).hashCode(), MoneyFactory.fromBigDecimal(new BigDecimal("50.4")).hashCode());

        String val2 = "1234567890123456789012";
        final Money bd1 = MoneyFactory.fromBigDecimal( new BigDecimal(val2) );
        final Money bd2 = MoneyFactory.fromString(val2);
        assertEquals( bd1, bd2 );
        assertEquals( bd1.hashCode(), bd2.hashCode() );
    }

    public void testNegate()
    {
        final Money lng1 = MoneyFactory.fromDouble(45.5, 1);
        final Money lng2 = MoneyFactory.fromDouble( -45.5, 1 );
        assertEquals( lng1, lng2.negate() );
        assertEquals( lng2, lng1.negate() );

        final Money bd1 = MoneyFactory.fromDouble( 355.555, 1 );
        final Money bd2 = MoneyFactory.fromDouble( -355.555, 1 );
        assertEquals( bd1, bd2.negate() );
        assertEquals( bd2, bd1.negate() );
    }

    public void testSubtract()
    {
        final Money lng1 = MoneyFactory.fromDouble(45.5, 1);
        final Money lng2 = MoneyFactory.fromString( "45.5" );
        assertEquals( "0", lng1.subtract( lng2 ).toString() );

        final Money bd1 = MoneyFactory.fromDouble( 355.555, 1 );
        final Money bd2 = MoneyFactory.fromString( "355.555" );
        assertEquals( "0", bd1.subtract( bd2 ).toString() );
    }

    public void testLongMultiply()
    {
        //long to long
        final Money lng1 = MoneyFactory.fromDouble(45.5, 1);
        final Money res1 = lng1.multiply( 5 );
        assertEquals( "227.5", res1.toString() );

        //bd to long
        final Money bd1 = MoneyFactory.fromDouble(45.55, 1);
        final Money res2 = bd1.multiply( 2 );
        assertTrue( res2 instanceof MoneyLong );
        assertEquals( "91.1", res2.toString() );
         //bd to bd
        String val1 = "0.000000000000000000000005";
        String val2 = "0.000000000000005";
        final Money bd2 = MoneyFactory.fromBigDecimal( new BigDecimal(val1) );
        assertTrue( bd2 instanceof MoneyBigDecimal );
        final Money res3 = bd2.multiply( 1 );
        assertTrue( res3 instanceof MoneyBigDecimal );
        assertEquals(val1, res3.toString());

        //bd to long (precision shrink)
        final Money res4 = bd2.multiply( 1000000000 );
        assertTrue( res4 instanceof MoneyLong );
        assertEquals( val2, res4.toString() );

        //test long overflow
        final Money lng2 = MoneyFactory.fromUnits( Long.MAX_VALUE, 0 );
        final Money res5 = lng2.multiply( 2 );
        assertTrue( res5 instanceof MoneyBigDecimal );
        final BigDecimal bdRes = BigDecimal.valueOf( Long.MAX_VALUE ).multiply( BigDecimal.valueOf( 2 ) );
        assertEquals( bdRes, res5.toBigDecimal() );


        final Money res6 = lng2.multiply( Long.MIN_VALUE );
        assertTrue( res6 instanceof MoneyBigDecimal );
        final BigDecimal bdRes2 = BigDecimal.valueOf( Long.MAX_VALUE ).multiply(BigDecimal.valueOf(Long.MIN_VALUE));
        assertEquals(bdRes2, res6.toBigDecimal());
    }

    public void testDoubleMultiply()
    {
        final Money lng1 = MoneyFactory.fromString("35.5" );
        //increase value, reduce precision
        final Money res1 = lng1.multiply( 2.0 );
        assertEquals( "71", res1.toString() );
        assertTrue( res1 instanceof MoneyLong );

        //decrease value, increase precision
        final Money res2 = lng1.multiply( 0.5 );
        assertTrue( res2 instanceof MoneyLong );
        assertEquals( "17.75", res2.toString() );

        //1/3 and 1/7
        final Money lng2 = MoneyFactory.fromDouble( 6, 1 );
        final Money res3 = lng2.multiply( 1.0 / 3.0 );
        assertEquals( "2", res3.toString() );
        assertTrue( res3 instanceof MoneyLong );

        final Money lng3 = MoneyFactory.fromDouble( 14, 1 );
        final Money res4 = lng3.multiply( 1.0 / 7.0 );
        assertEquals( "2", res4.toString() );
        assertTrue(res4 instanceof MoneyLong );

        //precision overflow, should get BD
        final Money lng4 = MoneyFactory.fromString( "0.5" );
        final Money res5 = lng4.multiply( 1.0 / 7.0 );  //0.5 / 7
        assertTrue( res5 instanceof MoneyBigDecimal );  //precision overflow

        String val1 = "0.000000000000000000000005";
        final Money bd1 = MoneyFactory.fromBigDecimal( new BigDecimal(val1) );
        assertTrue( bd1 instanceof MoneyBigDecimal );
        final Money res6 = bd1.multiply( 1000.75 );
        assertTrue(res6 instanceof MoneyBigDecimal);
    }

    public void testDivide()
    {
        //integer division
        final Money lng1 = MoneyFactory.fromDouble( 6.6, 1 );
        final Money res1 = lng1.divide( 2, 1 );
        assertEquals( 3.3, res1.toDouble() );
        final Money res1d = lng1.divide( 2.0, 1 );
        assertEquals( 3.3, res1d.toDouble() );
        //normal division
        final Money res2 = lng1.divide( 10, 3 );
        assertEquals( 0.66, res2.toDouble() );

        //bd division
        final BigDecimal tooLong = BigDecimal.ONE.divide( BigDecimal.valueOf( 3 ), MathContext.DECIMAL128 );
        final Money bd1 = MoneyFactory.fromBigDecimal( tooLong );
        assertTrue( bd1 instanceof MoneyBigDecimal );
        final Money res3 = bd1.divide( 2, 15 );
        assertTrue( res3.toString().startsWith( "0.1666"));

        //default double division
        final Money res4 = lng1.divide( 2.0, 2 );
        assertEquals( 3.3, res4.toDouble() );

        final Money res5 = res3.divide( 1.0 / 6.0, 6 );
        assertEquals( "1", res5.toString() );

        final Money res6 = bd1.divide( 1.0 / 3.0, 15 );
        assertEquals( "1", res6.toString() );

        //checking rounding
        final Money lng2 = MoneyFactory.fromUnits( 66, 2 );
        final Money res7 = lng2.divide( 2, 1 );
        assertEquals( 0.3, res7.toDouble() );
    }

    //this test checks if we can use multiplication by a negative power of 10 in conjunction with rounding
    public void testDivisionRounding()
    {
        long expectedLong = 0;
        double expected = 0.0;
        for( long i = 0; i < 10000; i += 2 )
        {
            if ( (i % 10 == 0) && ( (i / 10) % 2 == 1) )
            {
                expectedLong += 1;
                expected = expectedLong / 10.0;
            }
            final Money src = MoneyFactory.fromUnits( i, 2 );
            final Money res = src.divide( 2.0, 1 ); //it will always fit in 1 digit of precision
            assertEquals( expected, res.toDouble() );
        }
    }

    public void testTruncateLong()
    {
        final Money lng = MoneyFactory.fromString( "123.456" );
        assertEquals( "123.456", lng.truncate( 4 ).toString() );
        assertEquals( "123.456", lng.truncate( 3 ).toString() );
        assertEquals( "123.46", lng.truncate( 2 ).toString() );
        assertEquals( "123.5", lng.truncate( 1 ).toString() );
        assertEquals( "123", lng.truncate( 0 ).toString() );

        final Money lng2 = MoneyFactory.fromString( "123.123" );
        assertEquals( "123.123", lng2.truncate( 4 ).toString() );
        assertEquals( "123.123", lng2.truncate( 3 ).toString() );
        assertEquals( "123.12", lng2.truncate( 2 ).toString() );
        assertEquals( "123.1", lng2.truncate( 1 ).toString() );
        assertEquals( "123", lng2.truncate( 0 ).toString() );

        final Money lng3 = MoneyFactory.fromString( "123.003" );
        assertEquals( "123.003", lng3.truncate( 4 ).toString() );
        assertEquals( "123.003", lng3.truncate( 3 ).toString() );
        assertEquals( "123", lng3.truncate( 2 ).toString() );
        assertEquals( "123", lng3.truncate( 1 ).toString() );
        assertEquals( "123", lng3.truncate( 0 ).toString() );

        final Money lng4 = MoneyFactory.fromUnits( 995, 3 );
        assertEquals( "1", lng4.truncate( 2 ).toString() );

        final Money lng5 = MoneyFactory.fromUnits( 991, 3 );
        assertEquals( "0.99", lng5.truncate( 2 ).toString() );
    }

    public void testTruncateBd()
    {
        final Money val = MoneyFactory.fromDouble( 123.456, 1 );
        assertEquals( "123.456", val.truncate(4).toString() );
        assertEquals( "123.456", val.truncate(3).toString() );
        assertEquals( "123.46", val.truncate( 2 ).toString() );
        assertEquals( "123.5", val.truncate(1).toString() );
        assertEquals( "123", val.truncate(0).toString() );

        final Money val2 = MoneyFactory.fromDouble( 123.123, 1 );
        assertEquals("123.123", val2.truncate(4).toString());
        assertEquals("123.123", val2.truncate(3).toString());
        assertEquals("123.12", val2.truncate(2).toString());
        assertEquals("123.1", val2.truncate(1).toString());
        assertEquals( "123", val2.truncate( 0 ).toString() );

        final Money val3 = MoneyFactory.fromDouble( 123.003, 1 );
        assertEquals("123.003", val3.truncate(4).toString());
        assertEquals("123.003", val3.truncate(3).toString());
        assertEquals( "123", val3.truncate( 2 ).toString() );
        assertEquals("123", val3.truncate(1).toString());
        assertEquals( "123", val3.truncate( 0 ).toString() );
    }

    public void testCompareTo()
    {
        final Money v1 = new MoneyLong( 20, 1 );
        final Money v2 = new MoneyLong( 200, 2 );
        final Money v3 = new MoneyLong( 400, 2 );
        final Money v4 = new MoneyLong( 4000, 3 );
        assertEquals( 0, v1.compareTo( v2 ) );
        assertEquals( 0, v3.compareTo( v4 ) );
        assertTrue( v1.compareTo( v3 ) < 0 );
        assertTrue( v3.compareTo( v2 ) > 0 );
        assertTrue( v4.compareTo( v2 ) > 0 );
    }

    public void testTicket11()
    {
        final Money total = MoneyFactory.fromString( "5.5999999523162842" );
        final Money x = MoneyFactory.fromDouble( 5 );
        final Money res = total.subtract( x );
        assertNotNull( res ); //simply should not throw an exception
    }
	
	public void testMultiplicationIssue14() 
	{
		// allocation is 30%
        double allocation = 30;
        // total value
        Money totalValue = MoneyFactory.fromString("7281.6612");
        // money value of 30% allocation
        double value = allocation * totalValue.toDouble() / 100;
        Money moneyValue = MoneyFactory.fromDouble(value);

        // calculate the percentage of the money value
        double currentAllocationD = moneyValue.multiply(100)
            .divide(totalValue.toDouble(), 4)
            .toDouble();

        Money currentAllocation = moneyValue.multiply(100)
            .divide(totalValue.toDouble(), 4);

        // it should be 30, as set initially.
		assertEquals( 30d, currentAllocationD);
		assertEquals( "30", currentAllocation.toString());
	}
}
