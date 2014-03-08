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

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Some shared methods are kept here
 */
abstract class AbstractMoney implements Money {
    /**
     * Add another Money object to this one.
     *
     * @param other Other Money object
     * @return A new Money object normalized to the efficient representation if possible
     */
    public Money add( final Money other ) {
        if ( other instanceof MoneyLong )
            return add( ( MoneyLong ) other );
        else
            return add( ( MoneyBigDecimal ) other );
    }

    protected abstract Money add( final MoneyLong other );

    protected Money add( final MoneyBigDecimal other )
    {
        final BigDecimal res = toBigDecimal().add( other.toBigDecimal(), MathContext.DECIMAL64 );
        return MoneyFactory.fromBigDecimal( res );
    }

    /**
     * Subtract another Money object from this one.
     *
     * @param other Other money object
     * @return A new Money object normalized to the efficient representation if possible
     */
    public Money subtract( final Money other )
    {
        return add( other.negate() );
    }

}

