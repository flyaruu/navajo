package com.dexels.navajo.functions;

import com.dexels.navajo.parser.*;
import com.dexels.navajo.document.Message;
import com.dexels.navajo.document.Navajo;
import java.util.ArrayList;
import com.dexels.navajo.document.NavajoException;
import com.dexels.navajo.document.Property;
import com.dexels.navajo.server.SystemException;
import com.dexels.navajo.document.NavajoFactory;
import com.dexels.navajo.document.types.*;


/**
 * <p>Title: Navajo Product Project</p>
 * <p>Description: This is the official source for the Navajo server</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Dexels BV</p>
 * @author Arjen Schoneveld
 * @version 1.0
 */

public class SumProperties extends FunctionInterface {

  public String remarks() {
    return "Sums all properties in an array message";
  }
  public Object evaluate() throws com.dexels.navajo.parser.TMLExpressionException {

    if (getOperands().size() < 2) {
      for (int i = 0; i < getOperands().size(); i++) {
         Object o = getOperands().get(i);
        System.err.println("Operand # "+i+" is: "+o.toString()+" - "+ o.getClass());
      }
      throw new TMLExpressionException(this, "Wrong number of arguments: "+getOperands().size());
    }
    if (!(getOperand(0) instanceof String && getOperand(1) instanceof String)) {
      throw new TMLExpressionException(this, "Wrong argument types: " + getOperand(0).getClass() + " and " + getOperand(1).getClass());
    }
    String messageName = (String) getOperand(0);
    String propertyName = (String) getOperand(1);
    String filter = null;
    if (getOperands().size() > 2) {
      filter = (String) getOperand(2);
    }
    Message parent = getCurrentMessage();
    Navajo doc = getNavajo();

    try {
      ArrayList arrayMsg = (parent != null ? parent.getMessages(messageName) :
                            doc.getMessages(messageName));
      if (arrayMsg == null)
        throw new TMLExpressionException(this, "Empty or non existing array message: " + messageName);

      String sumType = "int";
      double sum = 0;
      for (int i = 0; i < arrayMsg.size(); i++) {
        Message m = (Message) arrayMsg.get(i);
        Property p = (Property) m.getProperty(propertyName);
        boolean evaluate = (filter != null ?  Condition.evaluate(filter, doc, null, m) : true);
        if (evaluate) {
          if (p != null) {
            Object o = p.getTypedValue();
            if (o==null) {
              continue;
            }
            if ((o!=null) && !(o instanceof Integer || o instanceof Double || o instanceof Float || o instanceof Money)) {
              throw new TMLExpressionException(this, "Only numbers are supported a sum. Not: "+(o==null?"null":o.getClass().toString()));
            }
            if (o instanceof Integer) {
              sumType = "int";
              sum += ((Integer) o).doubleValue();
            }
            else if (o instanceof Double) {
//              if (!((Double)o).equals(new Double(Double.NaN))) {
                sumType = "float";
                sum += ((Double) o).doubleValue();
//              }
            } else if (o instanceof Float) {
//              if (!((Float)o).equals(new Float(Float.NaN))) {
                sumType = "float";
                sum += ( (Float) o).doubleValue();
//              }
            } else if (o instanceof Money) {
//              if (!new Double(((Money)o).doubleValue()).equals(new Double(Float.NaN))) {
                sumType = "money";
                sum += ( (Money) o).doubleValue();
//              }
            }

          } else {
            throw new TMLExpressionException(this, "Property does not exist: " + propertyName);
          }
        }
      }
      if (sumType.equals("int"))
        return new Integer((int) sum);
      else if (sumType.equals("money")) {
        return new Money(sum);
      } else
        return new Double(sum);
    } catch (NavajoException ne) {
      throw new TMLExpressionException(this, ne.getMessage());
    } catch (SystemException se) {
      throw new TMLExpressionException(this, se.getMessage());
    }
  }
  public String usage() {
   return "SumProperties(<Array message name>,<Property name>[,<filter>])";
  }

  public static void main(String [] args) throws Exception {
    Navajo doc = NavajoFactory.getInstance().createNavajo();
    Message array = NavajoFactory.getInstance().createMessage(doc, "MyArray", Message.MSG_TYPE_ARRAY);
    doc.addMessage(array);
    for (int i = 0; i < 9; i++) {
      Message elt = NavajoFactory.getInstance().createMessage(doc, "MyArray", Message.MSG_TYPE_ARRAY_ELEMENT);
      array.addMessage(elt);
      Property p = NavajoFactory.getInstance().createProperty(doc, "MyInteger", Property.FLOAT_PROPERTY, i+".5", 0, "", Property.DIR_OUT);
      elt.addProperty(p);
    }
    doc.write(System.err);
    Operand result = Expression.evaluate("SumProperties('MyArray', 'MyInteger', '[MyInteger] > 3')", doc);
    System.err.println("result = " + result.value);
  }

}
