/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.javaperformance.money;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.testng.annotations.Test;

/**
 *
 * @author ratcash
 */
public class Log10Test {
    
    public Log10Test() {
    }
    
    @Test
    public void testLog10Floor() {
        assertThat(Log10.log10Floor(999999999), is(equalTo(8)));
        assertThat(Log10.log10Floor(99999999), is(equalTo(7)));
        assertThat(Log10.log10Floor(9999999), is(equalTo(6)));
        assertThat(Log10.log10Floor(999999), is(equalTo(5)));
        assertThat(Log10.log10Floor(99999), is(equalTo(4)));
        assertThat(Log10.log10Floor(9999), is(equalTo(3)));
        assertThat(Log10.log10Floor(999), is(equalTo(2)));
        assertThat(Log10.log10Floor(99), is(equalTo(1)));
        assertThat(Log10.log10Floor(9), is(equalTo(0)));
        //assertThat(Log10.log10Floor(0), is(equalTo(0)));
        
        assertThat(Log10.log10Floor(1234600010678l), is(equalTo(12)));
        assertThat(Log10.log10Quick(123456789000000000l), is(equalTo(17)));
    }

    @Test
    public void testLog10Quick() {
        assertThat(Log10.log10Quick(999999999), is(equalTo(8)));
        assertThat(Log10.log10Quick(99999999), is(equalTo(7)));
        assertThat(Log10.log10Quick(9999999), is(equalTo(6)));
        assertThat(Log10.log10Quick(999999), is(equalTo(5)));
        assertThat(Log10.log10Quick(99999), is(equalTo(4)));
        assertThat(Log10.log10Quick(9999), is(equalTo(3)));
        assertThat(Log10.log10Quick(999), is(equalTo(2)));
        assertThat(Log10.log10Quick(99), is(equalTo(1)));
        assertThat(Log10.log10Quick(9), is(equalTo(0)));
        assertThat(Log10.log10Quick(0), is(equalTo(0)));
        
        assertThat(Log10.log10Quick(1234600010678l), is(equalTo(12)));
        assertThat(Log10.log10Quick(123456789000000000l), is(equalTo(17)));
    }
    
}
