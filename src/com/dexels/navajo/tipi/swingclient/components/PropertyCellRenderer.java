package com.dexels.navajo.tipi.swingclient.components;

import javax.swing.table.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.swing.event.CellEditorListener;
import com.dexels.navajo.document.*;
import com.dexels.navajo.document.types.*;

import javax.swing.tree.TreeCellRenderer;
import javax.swing.border.*;
import java.awt.event.*;
import java.beans.*;

import javax.swing.border.LineBorder;
import java.text.*;


//import com.dexels.navajo.document.nanoimpl.*;
//import com.dexels.sportlink.client.swing.components.treetable.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Dexels.com</p>
 * @author unascribed
 * @version 1.0
 */

public class PropertyCellRenderer implements TableCellRenderer, ListCellRenderer {

  private String myPropertyType = null;
  private PropertyBox myPropertyBox = null;
  private MultiSelectPropertyBox myMultiSelectPropertyBox = null;
  private TextPropertyField myPropertyField = null;
  private JLabel formatStringField = null;
  private PropertyCheckBox myPropertyCheckBox = null;
  private DatePropertyField myDatePropertyField = null;
  private IntegerPropertyField myIntegerPropertyField = null;
  private FloatPropertyField myFloatPropertyField = null;
  private MoneyField myMoneyPropertyField = null;
  private PercentagePropertyField myPercentagePropertyField = null;
  private ClockTimeField myClockTimeField = null;
  private StopwatchTimeField myStopwatchTimeField = null;
  private PropertyPasswordField myPasswordField = null;
   private Property myProperty = null;
  private Map columnAttributes;
  private MessageTable myTable;
  private TableCellRenderer def = new DefaultTableCellRenderer();
  private final JLabel l;
  private JButton rowButton = new JButton();
  JLabel readOnlyField = new JLabel();

  Border b = new LineBorder(Color.black, 2);
  Border c = new LineBorder(Color.lightGray, 2);

  public PropertyCellRenderer() {
    //
    myPropertyField = new TextPropertyField();
    l = new JLabel() {
    	public boolean isOpaque() {
    		return true;
    	}
    };
    l.setPreferredSize(new Dimension(l.getPreferredSize().width, 17));
    l.setFont(myPropertyField.getFont());
    
    rowButton.setMargin(new Insets(0,1,0,1));
    rowButton.setFont(new Font(rowButton.getFont().getName(), Font.BOLD, rowButton.getFont().getSize()));
  }



  private Color selectedColor = new Color(220, 220, 255);
//  private Color selectedColor = new Color(255, 217, 115);
  private Color highColor = new Color(255, 255, 255);
  private Color lowColor = new Color(240, 240, 240);


  public void setHighColor(Color c) {
    highColor = c;
  }

  public void setLowColor(Color c) {
    lowColor = c;
  }

  public void setSelectedColor(Color c) {
    selectedColor = c;
  }

  public Color getSelectedColor() {
	return selectedColor;	
  }

  public Color getLowColor() {
		return lowColor;	
	  }

  public Color getHighColor() {
		return highColor;	
	  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    TableModel tm = table.getModel();
    if(!table.hasFocus()) {
    	hasFocus = false;
    }	
    boolean disabled = false;
    if(table.getSelectedRow() == row) {
      isSelected = true;
    }
//    if(isSelected && table.getEditingColumn() ==  column){

    if(MessageTable.class.isInstance(table)) {
      myTable = (MessageTable)table;
      columnAttributes = myTable.getColumnAttributes();
    }

    if(value==null) {
    	setComponentColor(l, isSelected, row, column, false,tm.getRowCount(), disabled);
        System.err.println("Returning null component");
    	return l;
    }
    
    if(Integer.class.isInstance(value)){
      rowButton.setText(""+(row+1));
      return rowButton;
    }

    if(Property.class.isInstance(value)) {
//      System.err.println("Yes, a property");
      myProperty = (Property)value;
      myPropertyType = myProperty.getType();
      
      JComponent propComponent = createComponent(myProperty);
      PropertyControlled pc = (PropertyControlled)propComponent;
     pc.setProperty(myProperty);
      setComponentColor(propComponent, isSelected, row, column, false,tm.getRowCount(), disabled);
      if (hasFocus)	 {
          if (myTable != null && myTable.isCellEditable(row, column)) {
        	  propComponent.setBorder(b);
          }
          else {
        	  propComponent.setBorder(c);
          }
        } else {
        	propComponent.setBorder(BorderFactory.createEmptyBorder());
        	
        }
      
      if(true) {
    	  return propComponent;
      }
      
      if(myPropertyType.equals(Property.SELECTION_PROPERTY)) {
        if(myProperty.getCardinality().equals("+")){
          if (myMultiSelectPropertyBox == null) {
              myMultiSelectPropertyBox = new MultiSelectPropertyBox();
            }
            myMultiSelectPropertyBox.setProperty(myProperty);
            setComponentColor(myMultiSelectPropertyBox, isSelected, row, column, false,tm.getRowCount(), disabled);
            myMultiSelectPropertyBox.setBorder(null);
            if (hasFocus) {
              if (myTable != null && myTable.isCellEditable(row, column)) {
                myMultiSelectPropertyBox.setBorder(b);
              }
              else {
                myMultiSelectPropertyBox.setBorder(c);
              }
            }

            return myMultiSelectPropertyBox;

        }else{

          if (myPropertyBox == null) {
            myPropertyBox = new PropertyBox();
          }
          myPropertyBox.setProperty(myProperty);
          setComponentColor(myPropertyBox, isSelected, row, column, false,
                            tm.getRowCount(), disabled);
          myPropertyBox.setBorder(null);
          myPropertyBox.setOpaque(true);
          if (hasFocus) {
            if (myTable != null && myTable.isCellEditable(row, column)) {
            	myPropertyBox.setBorder(b);
             
             }
            else {
              myPropertyBox.setBorder(c);
            }
          }

          return myPropertyBox;
        }
        /** @todo maybe we should change this so the renderer only loads the selected value */
      }
      if(myPropertyType.equals(Property.BOOLEAN_PROPERTY)) {
        if(myPropertyCheckBox == null) {
          myPropertyCheckBox = new PropertyCheckBox();
        }
        myPropertyCheckBox.setProperty(myProperty);
        if(hasFocus) {
          if(myTable != null && myTable.isCellEditable(row, column)) {
            myPropertyCheckBox.setBorder(b);
          } else {
            myPropertyCheckBox.setBorder(c);
          }
        }
        myPropertyCheckBox.setOpaque(true);
        setComponentColor(myPropertyCheckBox, isSelected, row, column, false, tm.getRowCount(), disabled);
        
        return myPropertyCheckBox;
      }
      if(myPropertyType.equals(Property.DATE_PROPERTY)) {
        if(myDatePropertyField == null) {
          myDatePropertyField = new DatePropertyField();
          myDatePropertyField.setShowCalendarPickerButton(false);
          myDatePropertyField.setReadOnly(true);
        }
        myDatePropertyField.setProperty(myProperty);
         myDatePropertyField.setOpaque(true);
        if(hasFocus) {
          if(myTable != null && myTable.isCellEditable(row, column)) {
            myDatePropertyField.setBorder(b);
          } else {
            myDatePropertyField.setBorder(c);
          }
        }
        setComponentColor(myDatePropertyField, isSelected, row, column, false, tm.getRowCount(), disabled);

        return myDatePropertyField;
      }
      if(myPropertyType.equals(Property.INTEGER_PROPERTY)) {
        if(myIntegerPropertyField == null) {
          myIntegerPropertyField = new IntegerPropertyField();
          myIntegerPropertyField.setReadOnly(true);
        }
        myIntegerPropertyField.setProperty(myProperty);
        setComponentColor(myIntegerPropertyField, isSelected, row, column, false, tm.getRowCount(), disabled);
        if(hasFocus) {
          if(myTable != null && myTable.isCellEditable(row, column)) {
            myIntegerPropertyField.setBorder(b);
          } else {
            myIntegerPropertyField.setBorder(c);
          }
        }

        return myIntegerPropertyField;
      }

      if(myPropertyType.equals(Property.FLOAT_PROPERTY)) {
        if(myFloatPropertyField == null) {
          myFloatPropertyField = new FloatPropertyField();
          myFloatPropertyField.setReadOnly(true);
        }
        myFloatPropertyField.setProperty(myProperty);
        setComponentColor(myFloatPropertyField, isSelected, row, column, false, tm.getRowCount(), disabled);
        if(hasFocus) {
          if(myTable != null && myTable.isCellEditable(row, column)) {
            myFloatPropertyField.setBorder(b);
          } else {
            myFloatPropertyField.setBorder(c);
          }
        }

        return myFloatPropertyField;
      }

      if(myPropertyType.equals(Property.MONEY_PROPERTY)) {
        if(myMoneyPropertyField == null) {
          myMoneyPropertyField = new MoneyField();
        }
        myMoneyPropertyField.setProperty(myProperty);
//        myMoneyPropertyField.pr(null);
        setComponentColor(myMoneyPropertyField, isSelected, row, column, false, tm.getRowCount(), disabled);
        if(hasFocus) {
          if(myTable != null && myTable.isCellEditable(row, column)) {
            myMoneyPropertyField.setBorder(b);
          } else {
            myMoneyPropertyField.setBorder(c);
          }
        }
        myMoneyPropertyField.updateColor((Money) myProperty.getTypedValue());
        return myMoneyPropertyField;
      }
      if(myPropertyType.equals(Property.PERCENTAGE_PROPERTY)) {
        if(myPercentagePropertyField == null) {
          myPercentagePropertyField = new PercentagePropertyField();
//          myPercentagePropertyField.setReadOnly(true);
        }
        myPercentagePropertyField.setProperty(myProperty);
        myPercentagePropertyField.doFocusLost(null);
        setComponentColor(myPercentagePropertyField, isSelected, row, column, false, tm.getRowCount(), disabled);
        if(hasFocus) {
          if(myTable != null && myTable.isCellEditable(row, column)) {
            myPercentagePropertyField.setBorder(b);
          } else {
            myPercentagePropertyField.setBorder(c);
          }
        }

        return myPercentagePropertyField;
      }

      if(myPropertyType.equals(Property.CLOCKTIME_PROPERTY)) {
        if(myClockTimeField == null) {
          myClockTimeField = new ClockTimeField();
          myClockTimeField.showSeconds(false);
        }
        myClockTimeField.setProperty(myProperty);
        setComponentColor(myClockTimeField, isSelected, row, column, false, tm.getRowCount(), disabled);

        return myClockTimeField;
      }

      if(myPropertyType.equals(Property.STOPWATCHTIME_PROPERTY)) {
        if(myStopwatchTimeField == null) {
          myStopwatchTimeField = new StopwatchTimeField();
        }
        myStopwatchTimeField.setProperty(myProperty);
        setComponentColor(myStopwatchTimeField, isSelected, row, column, false, tm.getRowCount(), disabled);
        updateComponentBorder(myStopwatchTimeField,hasFocus, row, column);
        return myStopwatchTimeField;
      }


      if(myPropertyType.equals(Property.STRING_PROPERTY)) {
        if(myProperty.isDirIn()) {
          if(myPropertyField == null) {
            myPropertyField = new TextPropertyField();
          }
          myPropertyField.setCapitalizationMode(null);
          myPropertyField.setProperty(myProperty);
          setComponentColor(myPropertyField, isSelected, row, column, false, tm.getRowCount(), disabled);
          updateComponentBorder(myPropertyField,hasFocus, row, column);
          return myPropertyField;

        } else {
          if(formatStringField == null) {
            formatStringField = new JLabel();
            if(myPropertyField == null) {
              myPropertyField = new TextPropertyField();
            }
            formatStringField.setFont(myPropertyField.getFont());
          }
//          formatStringField.setOpaque(true);
          Object o = myProperty.getTypedValue();
          if(o != null) {
            formatStringField.setText("" + o);
          } else {
            formatStringField.setText("");
          }
//          if(hasFocus) {
//            formatStringField.setBorder(c);
//          }
          setComponentColor(formatStringField, isSelected, row, column, false, tm.getRowCount(), disabled);
          updateComponentBorder(formatStringField,hasFocus, row, column);
          
          return formatStringField;

        }

      }
      // Added memo property for extended Tooltip text... see if it works out ok.
      if(myPropertyType.equals(Property.MEMO_PROPERTY)) {

        if(myPropertyField == null) {
          myPropertyField = new TextPropertyField();
        }
//      myPropertyField.setText(myProperty.getValue());
        myPropertyField.setProperty(myProperty);
        setComponentColor(myPropertyField, isSelected, row, column, false, tm.getRowCount(), disabled);
        myPropertyField.setToolTipText(myProperty.getValue());
//        updateComponentBorder(hasFocus, row, column);
        return myPropertyField;
      }

      if(myPropertyType.equals(Property.PASSWORD_PROPERTY)) {

        if(myPasswordField == null) {
          myPasswordField = new PropertyPasswordField();
        }
//      myPropertyField.setText(myProperty.getValue());
        myPasswordField.setProperty(myProperty);
        setComponentColor(myPasswordField, isSelected, row, column, false, tm.getRowCount(), disabled);
        myPasswordField.setToolTipText(myProperty.getValue());
        if(hasFocus) {
          if(myTable != null && myTable.isCellEditable(row, column)) {
            myPasswordField.setBorder(b);
          } else {
            myPasswordField.setBorder(c);
          }
        }
        return myPasswordField;
      }

      
      
//    System.err.println("Oh dear, strange property type...");
//      if (myPropertyField == null) {
//        myPropertyField = new TextPropertyField();
//      }
//      myPropertyField.setEditable(false);
//      Property temp = null;
//      try {
//        temp = NavajoFactory.getInstance().createProperty(NavajoFactory.getInstance().createNavajo(), "tmp",
//            Property.STRING_PROPERTY, "-", 1, "", Property.DIR_IN);
//      }
//      catch (NavajoException ex) {
//        ex.printStackTrace();
//      }
//      //    myProperty.setType(Property.UNKNOWN_PROPERTY);
//    myPropertyField.setProperty(temp);

//    myPropertyField.setName("unloaded_property");
//      myPropertyField.setBorder(null);

//      readOnlyField.setOpaque(true);
      setComponentColor(readOnlyField, isSelected, row, column, false, tm.getRowCount(), disabled);
      if(myTable != null && isSelected && column != -1 && column == myTable.getSelectedColumn()) {
//        readOnlyField.setBorder(disabled ? c : b);
        readOnlyField.setBorder(c);
      } else {
        readOnlyField.setBorder(null);
      }

      return readOnlyField;
//      if (Property.EXPRESSION_PROPERTY.equals(myProperty.getType())) {
//        setComponentColor(myPropertyField, isSelected, row, column, true, tm.getRowCount());
//      } else {
//        setComponentColor(myPropertyField, isSelected, row, column, false, tm.getRowCount());
//      }
//      if(hasFocus){
//            if(myTable.isCellEditable(row, column)){
//              myPropertyField.setBorder(b);
//            }else{
//              myPropertyField.setBorder(c);
//            }
//          }

    }

    return l;
  }

  
  private JComponent createComponent(Property p) {

	  String type = p.getType();
      if(type.equals(Property.EXPRESSION_PROPERTY)) {
          try {
        	  type = p.getEvaluatedType();
            if(type == null || "".equals(type)) {
            	type = myTable.getTypeHint(p.getName());
              if(type != null) {
                System.err.println("*******************\n**********************\n********************\n Hint found: " + myPropertyType);
              }
            }
            if(type == null || "".equals(type)) {
            	type = Property.STRING_PROPERTY;
            }
//            disabled = true;
          } catch(Throwable ex1) {
            ex1.printStackTrace();
          }
        }
	  if(type.equals(Property.PASSWORD_PROPERTY)) {
          if(myPasswordField == null) {
            myPasswordField = new PropertyPasswordField();
          }
          return myPasswordField;
      }
      if(type.equals(Property.PASSWORD_PROPERTY)) {
          if(myStopwatchTimeField == null) {
        	  myStopwatchTimeField = new StopwatchTimeField();
          }
          return myStopwatchTimeField;
      }
      if(type.equals(Property.BOOLEAN_PROPERTY)) {
          if(myPropertyCheckBox == null) {
        	  myPropertyCheckBox = new PropertyCheckBox();
          }
          return myPropertyCheckBox;
      }
      if(type.equals(Property.PERCENTAGE_PROPERTY)) {
          if(myPercentagePropertyField == null) {
        	  myPercentagePropertyField = new PercentagePropertyField();
          }
          return myPercentagePropertyField;
      }
      if(type.equals(Property.MONEY_PROPERTY)) {
          if(myMoneyPropertyField == null) {
        	  myMoneyPropertyField = new MoneyField();
          }
          return myMoneyPropertyField;
      }
      if(type.equals(Property.INTEGER_PROPERTY)) {
          if(myIntegerPropertyField == null) {
        	  myIntegerPropertyField = new IntegerPropertyField();
          }
          return myIntegerPropertyField;
      }
      if(type.equals(Property.FLOAT_PROPERTY)) {
          if(myFloatPropertyField == null) {
        	  myFloatPropertyField = new FloatPropertyField();
          }
          return myFloatPropertyField;
      }
      if(type.equals(Property.CLOCKTIME_PROPERTY)) {
          if(myClockTimeField == null) {
        	  myClockTimeField = new ClockTimeField();
          }
          return myClockTimeField;
      }
      if(type.equals(Property.DATE_PROPERTY)) {
          if(myDatePropertyField == null) {
        	  myDatePropertyField = new DatePropertyField();
              myDatePropertyField.setShowCalendarPickerButton(false);

          }
          return myDatePropertyField;
      }
      if(type.equals(Property.SELECTION_PROPERTY)) {
          if(p.getCardinality().equals("+")){
            if (myMultiSelectPropertyBox == null) {
                myMultiSelectPropertyBox = new MultiSelectPropertyBox();
              }
            return myMultiSelectPropertyBox;
          }else{
            if (myPropertyBox == null) {
              myPropertyBox = new PropertyBox();
            }
            return myPropertyBox;
          }
      }
//      if(type.equals(Property.STRING_PROPERTY) || type.equals(Property.MEMO_PROPERTY)) {
          if(myPropertyField == null) {
        	  myPropertyField = new TextPropertyField();
          }
          return myPropertyField;
//      }
  }
  
  
private void updateComponentBorder(JComponent vc, boolean hasFocus, int row, int column) {
	if(hasFocus) {
	    if(myTable != null && myTable.isCellEditable(row, column)) {
	      vc.setBorder(b);
	    } else {
	      vc.setBorder(c);
	    }
	  } else {
		   vc.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 2, 0, 2)), null));
	  }
}

  public void setComponentColor(Component c, boolean isSelected, int row, int col, boolean loading, int rowcount, boolean isDisabled) {
    /** @todo Check this a bit */
//    Message m = myTable.getMessageModel().getMessageRow(row);
//    m.write(System.err);

    JComponent cc = (JComponent)c;
//    cc.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 2, 0, 2)), null));
    
//    cc.setBackground(new Color(255,20,120));
//    cc.setOpaque(true		);
    if(isSelected) {
    	
        c.setBackground(selectedColor);
      } else {
    	  if(row % 2 == 0) {
        c.setBackground(highColor);
      } else {
        c.setBackground(lowColor);
      }
      }
      /*
    // hmf. lamaar
//     cc.setForeground(isSelected?SystemColor.textHighlightText:SystemColor.textText);
    if(PropertyField.class.isInstance(cc)) {
      PropertyField pf = (PropertyField)cc;
      if(isDisabled) {
        pf.setForeground(pf.getOriginalDisabledColor());
      } else {
        pf.setForeground(pf.getOriginalForeground());
      }
    } else {
      if(isDisabled) {
        cc.setForeground(Color.GRAY);
      } else {
        cc.setForeground(Color.black);
      }
    }
//       cc.setEnabled(!isDisabled);

    if(isSelected && col > -1 && myTable != null) {
      Border b = new CompoundBorder(new LineBorder(Color.black, 2), new EmptyBorder(new Insets(0, 2, 0, 2)));
//       if(myTable.isCellEditable(row, col)){
      b = new LineBorder(Color.black, 2);
//       }
    }

    if(isSelected) {
      c.setBackground(selectedColor);
    } else {
      if(loading) {
        if(row % 2 == 0) {
          c.setBackground(new Color(255, 230, 230));
        } else {
          c.setBackground(new Color(255, 200, 200));
        }
//        c.setBackground(Color.pink);
      } else {

      }
    }
    if(myProperty == null) {
      return;
    }
    */
    if(columnAttributes != null) {
      ColumnAttribute ca = (ColumnAttribute)columnAttributes.get(myProperty.getName());
      if(ca != null) {
        if(ca.getType().equals(ColumnAttribute.TYPE_ROWCOLOR)) {
          String color = ca.getParam(myProperty.getValue());
          if(color != null) {
            Color clr = Color.decode(color);
            if(isSelected) {
              // Darker color
              Color dark = clr.darker();
              clr = dark;
            }
            c.setBackground(clr);
            if (myTable != null) {
              myTable.setRowColor(row, clr);
            }
          }
        }
      } else {
        Color clr = (myTable != null ? myTable.getRowColor(row) : null);
        if(clr != null) {
          Color dark = clr.darker();
          c.setBackground(isSelected ? dark : clr);
        }
      }
    }
  }


  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//    System.err.println("Getting renderer for row: "+index);
    Selection sel = (Selection)value;
//    l.setOpaque(true);
    l.setText(sel == null ? "" : sel.toString());
    setComponentColor(l, isSelected, index, -1, false, list.getModel().getSize(), false);
    return l;
  }


//  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//    JTreeTable.TreeTableCellRenderer tt;
//  }

  private String listPropertyType = null;

  public void setPropertyTypeForListCell(String s) {

  }

  public static void main(String[] args) {
    Locale[] locales = NumberFormat.getAvailableLocales();
    double myNumber = -1234.56;
    NumberFormat form;
    for(int j = 0;j < 4;++j) {
      System.out.println("FORMAT");
      for(int i = 0;i < locales.length;++i) {
        if(locales[i].getCountry().length() == 0) {
          continue; // Skip language-only locales
        }
        System.out.print(locales[i].getDisplayName());
        switch(j) {
        case 0:
          form = NumberFormat.getInstance(locales[i]);
          break;
        case 1:
          form = NumberFormat.getIntegerInstance(locales[i]);
          break;
        case 2:
          form = NumberFormat.getCurrencyInstance(locales[i]);
          break;
        default:
          form = NumberFormat.getPercentInstance(locales[i]);
          break;
        }
        if(form instanceof DecimalFormat) {
          System.out.print(": " + ((DecimalFormat)form).toPattern());
        }
        System.out.print(" -> " + form.format(myNumber));
        try {
          System.out.println(" -> " + form.parse(form.format(myNumber)));
        } catch(ParseException e) {}
      }
    }
  }


}
