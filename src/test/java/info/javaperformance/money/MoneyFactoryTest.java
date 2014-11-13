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

public class MoneyFactoryTest extends TestCase {
    public void testLongConversions()
    {
        double val = 0;
        for ( int i = 0; i < 10000; ++i )
        {
            final Money res = MoneyFactory.fromDouble( val, 1 );
            assertTrue( "Failed to convert " + val, res instanceof MoneyLong );
            //from/to String conversions
            final String resStr = res.toString();
            final Money resCopy = MoneyFactory.fromString( resStr );
            assertEquals( res, resCopy );

            val = res.toDouble() + 0.1;
        }
    }

    public void testBDConversions()
    {
        double val = 0.05;
        for ( int i = 0; i < 10000; ++i )
        {
            final Money res = MoneyFactory.fromDouble( val, 1 );
            assertTrue( "Failed to convert " + val, res instanceof MoneyBigDecimal );

            //from/to String conversions
            final String resStr = res.toString();
            final Money resCopy = MoneyFactory.fromString( resStr );
            assertEquals( res.toString(), resCopy.toString() );

            val = res.toDouble() + 0.1;
        }
    }

    public void testFromUnits()
    {
        for ( int i = 0; i < 10000; ++i )
        {
            final Money res = MoneyFactory.fromUnits(i, 1);
            assertTrue( "Failed to convert " + i, res instanceof MoneyLong );
        }
    }

    public void testToBd()
    {
        final Money money = MoneyFactory.fromUnits( 1, 1 );
        assertEquals( new BigDecimal( "0.1" ), money.toBigDecimal() );
    }

    public void testFromString()
    {
        //too long 1
        assertTrue( MoneyFactory.fromString( "1234567890.12345678901234567890" ) instanceof MoneyBigDecimal );
        //too long 2
        assertTrue( MoneyFactory.fromString( "123456789012345678901234567890" ) instanceof MoneyBigDecimal );
        try
        {
            MoneyFactory.fromString( "1234567890.12.12" );
            fail( "Should fail on invalid numbers" );
        }
        catch ( IllegalArgumentException ex )
        {
            //ok
        }
        try
        {
            MoneyFactory.fromString( "1234567890,1212" );
            fail( "Should fail on invalid numbers" );
        }
        catch ( IllegalArgumentException ex )
        {
            //ok
        }
    }

    public void testFromBd()
    {
        final Money lng1 = MoneyFactory.fromBigDecimal( new BigDecimal("0.00005") );
        assertTrue( lng1 instanceof MoneyLong );
        assertEquals("0.00005", lng1.toString());

        String val1 = "0.000000000000000000000005";
        final Money bd1 = MoneyFactory.fromBigDecimal( new BigDecimal(val1) );
        assertTrue( bd1 instanceof MoneyBigDecimal );
        assertEquals( val1, bd1.toString() );

        String val2 = "1234567890123456789012";
        final Money bd2 = MoneyFactory.fromBigDecimal( new BigDecimal(val2) );
        assertTrue( bd2 instanceof MoneyBigDecimal );
        assertEquals( val2, bd2.toString() );

        String val3 = "5000000000000000000000000000000";
        final Money bd3 = MoneyFactory.fromBigDecimal( new BigDecimal(val3) );
        assertTrue( bd3 instanceof MoneyBigDecimal );
        assertEquals( val3, bd3.toString() );

        String val4 = "123456789012345600000000000";
        final Money bd4 = MoneyFactory.fromBigDecimal( new BigDecimal(val4) );
        assertTrue( bd4 instanceof MoneyBigDecimal );
        assertEquals( val4, bd4.toString() );

        String val5 = "123456789012346000";
        final Money bd5 = MoneyFactory.fromBigDecimal( new BigDecimal(val5) );
        assertTrue( bd5 instanceof MoneyLong );
        assertEquals( val5, bd5.toString() );
    }

    public void testFromCharArray()
    {
        final char[] buffer = new char[ 100 ];
        toCharArrayHelper(buffer, "1234567.8901");
        toCharArrayHelper(buffer, "1234567.89");
        toCharArrayHelper(buffer, "1234567.8");
        toCharArrayHelper(buffer, "1234567");
        toCharArrayHelper(buffer, "123456");
        toCharArrayHelper(buffer, "12345");
    }

    public void testFromByteArray()
    {
        final byte[] buffer = new byte[ 100 ];
        toByteArrayHelper(buffer, "1234567.8901");
        toByteArrayHelper(buffer, "1234567.89");
        toByteArrayHelper(buffer, "1234567.8");
        toByteArrayHelper(buffer, "1234567");
        toByteArrayHelper(buffer, "123456");
        toByteArrayHelper(buffer, "12345");
    }

    public void testSigns()
    {
        assertEquals( "1", MoneyFactory.fromString("+1").toString() );
        assertEquals( "-1", MoneyFactory.fromString("-1").toString() );
        assertEquals( "1", MoneyFactory.fromString("+1.0").toString() );
        assertEquals( "-1", MoneyFactory.fromString("-1.0").toString() );
    }

    private void toCharArrayHelper(final char[] buffer, final String number) {
        char[] chars = number.toCharArray();
        System.arraycopy(chars, 0, buffer, 10, chars.length);
        assertEquals( number, MoneyFactory.fromCharArray(buffer, 10, chars.length).toString() );
    }

    private void toByteArrayHelper(final byte[] buffer, final String number) {
        byte[] bytes = number.getBytes();
        System.arraycopy(bytes, 0, buffer, 10, bytes.length);
        assertEquals( number, MoneyFactory.fromByteArray(buffer, 10, bytes.length).toString() );
    }

    public void testManyDecimals() {
        // 8860415583022323200
        //  804798809343434368
        // 9665.214392365757568  //19 digits
        // 9223.372036854775807  //Long.MAX_VALUE

        Money sum = MoneyFactory.fromCharSequence(  "8860.4155830223232" );        //precision=13, digits=17
        Money toAdd = MoneyFactory.fromCharSequence( "804.798809343434368" );      //precision=15, digits=18
        Money newSum = sum.add( toAdd );
        assertTrue( newSum instanceof MoneyBigDecimal );
        assertTrue("Value of newSum is " + newSum, newSum.toDouble() > 0);
    }

    public void testManyDecimals2() {
        Money sum = MoneyFactory.fromCharSequence(  "9999998860.4155830223232" );
        Money toAdd = MoneyFactory.fromCharSequence( "999999804.798809343434368" );
        Money newSum = sum.add(toAdd);
        assertTrue( newSum instanceof MoneyBigDecimal );
        assertTrue("Value of newSum is " + newSum, newSum.toDouble() > 0);
    }


}
