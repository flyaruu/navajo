package com.dexels.navajo.functions;

import com.dexels.navajo.document.Operand;
import com.dexels.navajo.document.types.ClockTime;
import com.dexels.navajo.parser.Expression;
import com.dexels.navajo.parser.FunctionInterface;
import com.dexels.navajo.parser.TMLExpressionException;


/**
 * <p>Title: Navajo Product Project</p>
 * <p>Description: This is the official source for the Navajo server</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Dexels BV</p>
 * @author Arjen Schoneveld
 * @version $Id$
 *
 * DISCLAIMER
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL DEXELS BV OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */

public final class ToClockTime extends FunctionInterface {
  public ToClockTime() {
  }

  public String remarks() {
   return "Cast an object to a clocktime object";
  }

  public final Object evaluate() throws com.dexels.navajo.parser.TMLExpressionException {
    Object o = getOperand(0);
    if (o instanceof java.util.Date) {
      return new ClockTime((java.util.Date) o);
    } else if (o instanceof String) {
      try {
        return new ClockTime( (String) o);
      } catch (Exception e) {
        throw new TMLExpressionException("Invalid clocktime: " + o);
      }
    } else if (o instanceof ClockTime) {
    	return o;
    } else
      throw new TMLExpressionException("Invalid clocktime: " + o);
  }

  public String usage() {
    return "ToClockTime(Date/String/ClockTime): ClockTime";
  }

  public static void main(String [] args) throws Exception {

    // Tests.
    ToClockTime cct = new ToClockTime();
    cct.reset();
    cct.insertOperand("12");
    System.out.println("cct = " + cct.evaluate());

    String expr = "ToClockTime('09:00') + ToClockTime('10')";
    Operand o = Expression.evaluate(expr, null);
    System.out.println("9:00 > 10:00 ? " + o.value + ", ToClockTime('25:88') = " + new ClockTime("25:88").toString());

    expr = "ToClockTime('9') >= ToClockTime('9')";
    Operand o2 = Expression.evaluate(expr, null);
    System.out.println("9:00 >= 9:00 ? " + o2.value);

    expr = "ToClockTime('9') < ToClockTime('12')";
    Operand o3 = Expression.evaluate(expr, null);
    System.out.println("9:00 < 12:00 ? " + o3.value);


  }

}
