package br.com.caelum.stella.faces.validation;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.caelum.stella.ResourceBundleMessageProducer;
import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

/**
 * Caso ocorra algum erro de validação, todas as mensagens serão enfileiradas no
 * FacesContext e associadas ao elemento inválido.
 * 
 * @author Fabio Kung
 * @author Leonardo Bessa
 */
public class StellaCPFValidator implements javax.faces.validator.Validator,
		javax.faces.component.StateHolder {
	/**
	 * Identificador do Validador JSF.
	 */
	public static final String VALIDATOR_ID = "StellaCPFValidator";
	private boolean formatted;
	private boolean transientValue = false;

	/**
	 * Atribui se a regra de validação deve considerar, ou não, a cadeia no
	 * formato do documento.
	 * 
	 * @param formatted
	 *            caso seja <code>true</code> o validador considera que a
	 *            cadeia está formatada; caso contrário, considera que a cadeia
	 *            contém apenas dígitos numéricos.
	 */
	public void setFormatted(boolean formatted) {
		this.formatted = formatted;
	}

	public void validate(FacesContext facesContext, UIComponent uiComponent,
			Object value) throws ValidatorException {
		Application application = facesContext.getApplication();
		String bundleName = application.getMessageBundle();
		Locale locale = facesContext.getViewRoot().getLocale();
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);

		ResourceBundleMessageProducer producer = new ResourceBundleMessageProducer(bundle);
		CPFValidator validator = new CPFValidator(producer, formatted);

		try {
			validator.assertValid(value.toString());
		}
		catch (InvalidStateException e){
			List<ValidationMessage> messages = e.getValidationMessages();
			String firstErrorMessage = messages.remove(0).getMessage();
			registerAllMessages(facesContext, uiComponent, messages);
			throw new ValidatorException(new FacesMessage(firstErrorMessage));
		}
	}

	private void registerAllMessages(FacesContext facesContext,
			UIComponent uiComponent, List<ValidationMessage> messages) {
		for (ValidationMessage message : messages) {
			String componentId = uiComponent.getClientId(facesContext);
			facesContext.addMessage(componentId, new FacesMessage(message
					.getMessage()));
		}
	}

	public boolean isTransient() {
		return transientValue;
	}

	public void restoreState(FacesContext ctx, Object state) {
		this.formatted = (Boolean) state;
	}

	public Object saveState(FacesContext arg0) {
		return formatted;
	}

	public void setTransient(boolean transientValue) {
		this.transientValue = transientValue;
	}
}