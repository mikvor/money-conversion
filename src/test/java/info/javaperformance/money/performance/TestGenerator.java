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

import java.util.List;

/**
 * The main runner for tests capable to create result tables in HTML.
 */
public class TestGenerator {
    private static final int WARMUP = 20 * 1000;
    private static final int ITERS = 10 * 1000 * 1000;

    public static void main(String[] args) {
        final List<TestResult> from = new FromConversionTests().runAllTests( WARMUP, ITERS );
        final List<TestResult> to = new ToConversionTests().runAllTests( WARMUP, ITERS );
        final List<TestResult> ops = new OperationsTests( 1000 ).runAllTests( WARMUP, ITERS );

        System.out.println( "FromConversionTests:");
        System.out.println( getTable( from ) );
        System.out.println( "\n\n\n");

        System.out.println( "ToConversionTests:");
        System.out.println( getTable( to ) );
        System.out.println( "\n\n\n");

        System.out.println( "OperationTests:");
        System.out.println( getTable( ops ) );
        System.out.println( "\n\n\n");
    }

    private static String getTable( final List<TestResult> results )
    {
        final StringBuilder sb = new StringBuilder( 2048 );
        if ( isTwoColumn( results ) )
        {
            sb.append( "<table border=\"1\">\n<tr><td>Test name</td><td>MoneyLong (Kops/sec)</td><td>MoneyBigDecimal (Kops/sec)</td></tr>\n" );
            for ( final TestResult r : results )
                sb.append( "<tr><td>" + r.name + "</td><td>" + r.longRate + "</td><td>" + r.bdRate + "</td></tr>\n");
        }
        else
        {
            sb.append( "<table border=\"1\">\n<tr><td>Test name</td><td>Result (Kops/sec)</td></tr>\n" );
            for ( final TestResult r : results )
                sb.append( "<tr><td>" + r.name + "</td><td>" + r.longRate + "</td></tr>\n");
        }
        sb.append( "</table>\n" );
        return sb.toString();
    }

    private static boolean isTwoColumn( final List<TestResult> results )
    {
        for ( final TestResult r : results )
            if ( r.bdRate != 0 )
                return true;
        return false;
    }
}
