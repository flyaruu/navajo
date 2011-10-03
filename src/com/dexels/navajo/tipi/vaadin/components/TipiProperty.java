package com.dexels.navajo.tipi.vaadin.components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.dexels.navajo.document.NavajoException;
import com.dexels.navajo.document.Selection;
import com.dexels.navajo.document.notifier.SerializablePropertyChangeListener;
import com.dexels.navajo.tipi.TipiBreakException;
import com.dexels.navajo.tipi.TipiEventListener;
import com.dexels.navajo.tipi.TipiException;
import com.dexels.navajo.tipi.internal.PropertyComponent;
import com.dexels.navajo.tipi.vaadin.components.base.TipiVaadinComponentImpl;
import com.dexels.navajo.tipi.vaadin.document.SelectionBridge;
import com.dexels.navajo.tipi.vaadin.document.SelectionListBridge;
import com.dexels.navajo.tipi.vaadin.document.ValuePropertyBridge;
import com.vaadin.data.Container.PropertySetChangeEvent;
import com.vaadin.data.Container.PropertySetChangeListener;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;

public class TipiProperty extends TipiVaadinComponentImpl implements PropertyComponent {

	private static final long serialVersionUID = 142570190396385078L;
	private Label description;
	private Component value;
	private com.dexels.navajo.document.Property property;
	private String propertyName;
	private int label_indent;
	private HorizontalLayout container;
	
	private PropertyChangeListener myChangeListener = null;
	private boolean showLabel = false;
	private boolean forceReadOnly;
	private Integer width;
	private String selectiontype = "combo";
	private int memoColumnCount = 40;
	private int memoRowCount = 5;
	
	@Override
	public Object createContainer() {
		container = new HorizontalLayout();
		container.setMargin(false);
		description = new Label();
//		description.setHeight("12px");
		container.addComponent(description);
		return container;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}

	@Override
	public void addTipiEventListener(TipiEventListener listener) {
		
	}

	@Override
	public com.dexels.navajo.document.Property getProperty() {
		return this.property;
	}

	@Override
	public void setProperty(com.dexels.navajo.document.Property p) {
		if(this.property!=null && myChangeListener!=null) {
			this.property.removePropertyChangeListener(myChangeListener);
			myChangeListener = null;

		}
		this.property = p;

		myChangeListener = new SerializablePropertyChangeListener() {
			
			private static final long serialVersionUID = -2164154865756231662L;

			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				if(!"selection".equals(pce.getPropertyName())) {
					refreshPropertyValue();
				}
			}
		};

		this.property.addPropertyChangeListener(myChangeListener);
		
		this.refreshPropertyValue();
		if(this.width!=null) {
			value.setWidth(this.width, Sizeable.UNITS_PIXELS);
		}

			//		value.setCaption("Caption: "+p.getDescription());
	}
	protected void refreshPropertyValue() {
		if(this.property==null) {
			clearPropertyValue();
			return;
		}
		description.setVisible(showLabel);
		String descr = property.getDescription();
		if("".equals(descr)) {
			descr = null;
		}
		description.setValue(descr);
		
		if(value!=null) {
			container.removeComponent(value);
			value=null;
		}
		if(com.dexels.navajo.document.Property.SELECTION_PROPERTY.equals(property.getType())) {
			// create selection property
			try {
				createSelectionProperty();
			} catch (NavajoException e) {
				e.printStackTrace();
			}
		}
		if(com.dexels.navajo.document.Property.DATE_PROPERTY.equals(property.getType())) {
			// create date property
			createDateProperty();
		}
//		if(com.dexels.navajo.document.Property.CLOCKTIME_PROPERTY.equals(property.getType())) {
			// create date property
//			createClockTimeProperty();
//		}

		if(com.dexels.navajo.document.Property.BOOLEAN_PROPERTY.equals(property.getType())) {
			// create date property
			createBooleanProperty();
		}

		if (value==null) {

			createTextualProperty();
		}
		value.setCaption(null);
		value.setEnabled(isEditable());

	}

	private void createDateProperty() {
		DateField df = new DateField(new ValuePropertyBridge(property));
		df.setDateFormat("dd-MM-yyyy");
		df.setImmediate(true);
		value = df;
		addPropertyComponent(df);
		container.addComponent(df);
	}
	private void createBooleanProperty() {
		CheckBox df = new CheckBox("",new ValuePropertyBridge(property));
		df.setImmediate(true);
		value = df;
		addPropertyComponent(df);
		container.addComponent(df);
	}
	private void createSelectionProperty() throws NavajoException {
		if(com.dexels.navajo.document.Property.CARDINALITY_MULTIPLE.equals(property.getCardinality())) {
			createMultiSelect();
		} else {
			createSingleCardinality();
		}
	}

	protected void createMultiSelect() throws NavajoException {
		SelectionListBridge selectionListBridge = new SelectionListBridge(property);
		if("picklist".equals(selectiontype)) {
			value = new TwinColSelect("caption",property.getAllSelections());
		} else {
			if("list".equals(selectiontype)) {
				value = new ListSelect("",selectionListBridge);
				((ListSelect)value).setMultiSelect(true);
			} else {
				value = new OptionGroup("",selectionListBridge);
				((OptionGroup)value).setMultiSelect(true);
			}
		}
		addPropertyComponent(value);
		AbstractSelect t = (AbstractSelect)value;
		t.addListener(new PropertySetChangeListener() {
			

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void containerPropertySetChange(PropertySetChangeEvent event) {
				System.err.println("CHAAAANGE");
			}
		});
	}



	private void createSingleCardinality() {
			SelectionListBridge selectionListBridge = new SelectionListBridge(property);
//			SelectedItemValuePropertyBridge sivp = new SelectedItemValuePropertyBridge(property);
			value = new Select("",selectionListBridge);
			if("radio".equals(selectiontype)) {
				value = new OptionGroup("",selectionListBridge);
			} else {
				value = new Select("",selectionListBridge);
			}
			final AbstractSelect t = (AbstractSelect)value;
			
			property.addPropertyChangeListener(new SerializablePropertyChangeListener() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void propertyChange(PropertyChangeEvent pce) {
					if(pce.getPropertyName().equals("selection")) {
						System.err.println("Selection changed!");
					}
				}
			});
			
//			value = new OptionGroup("Combo:",new SelectionListBridge(property));
			t.setImmediate(true);
			addPropertyComponent(value);
			SelectionBridge selected = selectionListBridge.getSelected();
			if(selected!=null) {
				Property selectedName = selected.getItemProperty("name");
				t.setValue(selectedName.getValue());
			}
			
			t.addListener(new Property.ValueChangeListener() {
				
				private static final long serialVersionUID = 1696480526302969095L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					try {
						Selection ss = property.getSelection((String) event.getProperty().getValue());
						property.setSelected(ss);
						performTipiEvent("onValueChanged", null, true);
					} catch (TipiBreakException e) {
						e.printStackTrace();
					} catch (TipiException e) {
						e.printStackTrace();
					} catch (NavajoException e) {
						e.printStackTrace();
					}
				}
			});
	
	}

	private boolean isEditable() {
		return property.isDirIn() && !forceReadOnly;
	}
	private void createTextualProperty() {
		AbstractTextField p = null;
		if(com.dexels.navajo.document.Property.PASSWORD_PROPERTY.equals(property.getType())) {
			p = new PasswordField(new ValuePropertyBridge(property));
		} else {
			if(com.dexels.navajo.document.Property.MEMO_PROPERTY.equals(property.getType())) {
				p = new TextArea(new ValuePropertyBridge(property));
				p.setColumns(this.memoColumnCount);
				((TextArea)p).setRows(this.memoRowCount);
			} else {
				p = new TextField(new ValuePropertyBridge(property));
							
			}
		
		}
		p.setImmediate(true);
		value = p;
		final AbstractTextField q = p;
		p.addListener(new TextChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {
				TipiProperty.this.property.setAnyValue(q.getValue());
				System.err.println("Property value changed: " + q.getValue());
			}
		});
		addPropertyComponent(value);
	}

	private void addPropertyComponent(Component value) {
		container.addComponent(value);
		container.setExpandRatio(value, 1);
	}


	private void clearPropertyValue() {
		container.removeComponent(value);
		description.setVisible(false);
	}

	@SuppressWarnings("unused")
	private boolean isTextualProperty(com.dexels.navajo.document.Property p) {
		if(com.dexels.navajo.document.Property.SELECTION_PROPERTY.equals(p.getType())) {
			return false;
		}
		if(com.dexels.navajo.document.Property.BOOLEAN_PROPERTY.equals(p.getType())) {
			return false;
		}
		if(com.dexels.navajo.document.Property.DATE_PROPERTY.equals(p.getType())) {
			return false;
		}
		return true;
	}

	protected Object getComponentValue(String name) {
		if(name.equals("propertyValue")) {
			// ----
			return this.property.getTypedValue();
		}
		return super.getComponentValue(name);
	}

	protected void setComponentValue(String name, Object object) {
		if(name.equals("propertyValue")) {
			if(property!=null) {
				property.setAnyValue(object);
			}
		}
		if(name.equals("alwaysUseLabel")) {
			this.forceReadOnly  = (Boolean)object;
		}		
		if(name.equals("selectiontype")) {
			this.selectiontype   = (String)object;
		}		
		if(name.equals("showlabel")) {
			this.showLabel  = (Boolean)object;
		}
		// historical hack
		if(name.toLowerCase().equals("propertyname")) {
			this.propertyName = (String) object;
		}
		if(name.toLowerCase().equals("label_indent")) {
			this.label_indent = (Integer) object;
			description.setWidth(this.label_indent, Sizeable.UNITS_PIXELS);
		}
		if(name.toLowerCase().equals("memoColumnCount")) {
			this.memoColumnCount = (Integer) object;
		}
		if(name.toLowerCase().equals("memoRowCount")) {
			this.memoRowCount = (Integer) object;
		}

		if(name.toLowerCase().equals("width")) {
			this.width = (Integer) object;
			if(value!=null) {
				value.setWidth(this.width, Sizeable.UNITS_PIXELS);
				
			}
		}

		
		if(name.equals("visible")) {
			this.container.setVisible((Boolean)object);
		}
		
		super.setComponentValue(name, object);
	}

}
