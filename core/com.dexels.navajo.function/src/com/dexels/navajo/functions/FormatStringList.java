package com.dexels.navajo.functions;


import java.util.ArrayList;
import java.util.List;

import com.dexels.navajo.document.Operand;
import com.dexels.navajo.parser.Expression;
import com.dexels.navajo.parser.FunctionInterface;
import com.dexels.navajo.parser.TMLExpressionException;


/**
 * Title:        Navajo
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      Dexels
 * @author Arjen Schoneveld en Martin Bergman
 * @version $Id$
 */

public final class FormatStringList extends FunctionInterface {

    public FormatStringList() {}

    @SuppressWarnings("rawtypes")
	public final Object evaluate() throws com.dexels.navajo.parser.TMLExpressionException {
        Object a = this.getOperands().get(0);
        Object b = this.getOperands().get(1);

        if (a instanceof String)
            return (String) a;
        if (!(a instanceof ArrayList))
            throw new TMLExpressionException("FormatStringList: invalid operand: " + a.getClass().getName());
        if (!(b instanceof String))
            throw new TMLExpressionException("FormatStringList: invalid operand: " + a.getClass().getName());
        List strings = (List) a;
        String sep = (String) b;
        StringBuffer result = new StringBuffer(20 * strings.size());

        for (int i = 0; i < strings.size(); i++) {
            String el = (String) strings.get(i);

            result.append(el);
            if (i < (strings.size() - 1))
                result.append(sep);
        }
        return result.toString();
    }

    public String usage() {
        return "FormatStringList(list of Strings, separator). Example FormatStringList(\"{\"Navajo\", \"Dexels\"}\", \";\") returns \"Navajo;Dexels\"";
    }

    public String remarks() {
        return "Turns a list of strings in a single string using supplied delimiter.";
    }

    public static void main(String args[]) throws Exception {
        String expr = "Contains({'Aap', 'Noot'},'Vuur')";
        Operand o = Expression.evaluate(expr, null);
        System.err.println("o = " + o.value);
    }
}
