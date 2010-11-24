/*
* generated by Xtext
*/
package com.dexels.navajo.dsl.expression.ui.contentassist.antlr;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.RecognitionException;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.AbstractContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.FollowElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;

import com.google.inject.Inject;

import com.dexels.navajo.dsl.expression.services.NavajoExpressionGrammarAccess;

public class NavajoExpressionParser extends AbstractContentAssistParser {
	
	@Inject
	private NavajoExpressionGrammarAccess grammarAccess;
	
	private Map<AbstractElement, String> nameMappings;
	
	@Override
	protected com.dexels.navajo.dsl.expression.ui.contentassist.antlr.internal.InternalNavajoExpressionParser createParser() {
		com.dexels.navajo.dsl.expression.ui.contentassist.antlr.internal.InternalNavajoExpressionParser result = new com.dexels.navajo.dsl.expression.ui.contentassist.antlr.internal.InternalNavajoExpressionParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}
	
	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getPathElementAccess().getAlternatives(), "rule__PathElement__Alternatives");
					put(grammarAccess.getEqualityExpressionAccess().getAlternatives_2(), "rule__EqualityExpression__Alternatives_2");
					put(grammarAccess.getAdditiveExpressionAccess().getAlternatives_2(), "rule__AdditiveExpression__Alternatives_2");
					put(grammarAccess.getMultiplicativeExpressionAccess().getAlternatives_2(), "rule__MultiplicativeExpression__Alternatives_2");
					put(grammarAccess.getUnaryExpressionAccess().getAlternatives(), "rule__UnaryExpression__Alternatives");
					put(grammarAccess.getPrimaryExpressionAccess().getAlternatives(), "rule__PrimaryExpression__Alternatives");
					put(grammarAccess.getLiteralAccess().getAlternatives(), "rule__Literal__Alternatives");
					put(grammarAccess.getTopLevelAccess().getGroup(), "rule__TopLevel__Group__0");
					put(grammarAccess.getPathSequenceAccess().getGroup(), "rule__PathSequence__Group__0");
					put(grammarAccess.getPathSequenceAccess().getGroup_3(), "rule__PathSequence__Group_3__0");
					put(grammarAccess.getExistsTmlExpressionAccess().getGroup(), "rule__ExistsTmlExpression__Group__0");
					put(grammarAccess.getOrExpressionAccess().getGroup(), "rule__OrExpression__Group__0");
					put(grammarAccess.getOrExpressionAccess().getGroup_2(), "rule__OrExpression__Group_2__0");
					put(grammarAccess.getAndExpressionAccess().getGroup(), "rule__AndExpression__Group__0");
					put(grammarAccess.getAndExpressionAccess().getGroup_2(), "rule__AndExpression__Group_2__0");
					put(grammarAccess.getEqualityExpressionAccess().getGroup(), "rule__EqualityExpression__Group__0");
					put(grammarAccess.getEqualityExpressionAccess().getGroup_2_0(), "rule__EqualityExpression__Group_2_0__0");
					put(grammarAccess.getEqualityExpressionAccess().getGroup_2_1(), "rule__EqualityExpression__Group_2_1__0");
					put(grammarAccess.getAdditiveExpressionAccess().getGroup(), "rule__AdditiveExpression__Group__0");
					put(grammarAccess.getAdditiveExpressionAccess().getGroup_2_0(), "rule__AdditiveExpression__Group_2_0__0");
					put(grammarAccess.getAdditiveExpressionAccess().getGroup_2_1(), "rule__AdditiveExpression__Group_2_1__0");
					put(grammarAccess.getMultiplicativeExpressionAccess().getGroup(), "rule__MultiplicativeExpression__Group__0");
					put(grammarAccess.getMultiplicativeExpressionAccess().getGroup_2_0(), "rule__MultiplicativeExpression__Group_2_0__0");
					put(grammarAccess.getMultiplicativeExpressionAccess().getGroup_2_1(), "rule__MultiplicativeExpression__Group_2_1__0");
					put(grammarAccess.getUnaryExpressionAccess().getGroup_0(), "rule__UnaryExpression__Group_0__0");
					put(grammarAccess.getPrimaryExpressionAccess().getGroup_0(), "rule__PrimaryExpression__Group_0__0");
					put(grammarAccess.getPrimaryExpressionAccess().getGroup_1(), "rule__PrimaryExpression__Group_1__0");
					put(grammarAccess.getFunctionCallAccess().getGroup(), "rule__FunctionCall__Group__0");
					put(grammarAccess.getFunctionOperandsAccess().getGroup(), "rule__FunctionOperands__Group__0");
					put(grammarAccess.getFunctionOperandsAccess().getGroup_2(), "rule__FunctionOperands__Group_2__0");
					put(grammarAccess.getLiteralAccess().getGroup_0(), "rule__Literal__Group_0__0");
					put(grammarAccess.getLiteralAccess().getGroup_1(), "rule__Literal__Group_1__0");
					put(grammarAccess.getLiteralAccess().getGroup_2(), "rule__Literal__Group_2__0");
					put(grammarAccess.getLiteralAccess().getGroup_4(), "rule__Literal__Group_4__0");
					put(grammarAccess.getLiteralAccess().getGroup_5(), "rule__Literal__Group_5__0");
					put(grammarAccess.getLiteralAccess().getGroup_6(), "rule__Literal__Group_6__0");
					put(grammarAccess.getLiteralAccess().getGroup_6_3(), "rule__Literal__Group_6_3__0");
					put(grammarAccess.getLiteralAccess().getGroup_7(), "rule__Literal__Group_7__0");
					put(grammarAccess.getLiteralAccess().getGroup_8(), "rule__Literal__Group_8__0");
					put(grammarAccess.getLiteralAccess().getGroup_9(), "rule__Literal__Group_9__0");
					put(grammarAccess.getLiteralAccess().getGroup_10(), "rule__Literal__Group_10__0");
					put(grammarAccess.getTopLevelAccess().getToplevelExpressionAssignment_1(), "rule__TopLevel__ToplevelExpressionAssignment_1");
					put(grammarAccess.getOrExpressionAccess().getParametersAssignment_1(), "rule__OrExpression__ParametersAssignment_1");
					put(grammarAccess.getOrExpressionAccess().getOperationsAssignment_2_0(), "rule__OrExpression__OperationsAssignment_2_0");
					put(grammarAccess.getOrExpressionAccess().getParametersAssignment_2_1(), "rule__OrExpression__ParametersAssignment_2_1");
					put(grammarAccess.getAndExpressionAccess().getParametersAssignment_1(), "rule__AndExpression__ParametersAssignment_1");
					put(grammarAccess.getAndExpressionAccess().getOperationsAssignment_2_0(), "rule__AndExpression__OperationsAssignment_2_0");
					put(grammarAccess.getAndExpressionAccess().getParametersAssignment_2_1(), "rule__AndExpression__ParametersAssignment_2_1");
					put(grammarAccess.getEqualityExpressionAccess().getParametersAssignment_1(), "rule__EqualityExpression__ParametersAssignment_1");
					put(grammarAccess.getEqualityExpressionAccess().getOperationsAssignment_2_0_0(), "rule__EqualityExpression__OperationsAssignment_2_0_0");
					put(grammarAccess.getEqualityExpressionAccess().getParametersAssignment_2_0_1(), "rule__EqualityExpression__ParametersAssignment_2_0_1");
					put(grammarAccess.getEqualityExpressionAccess().getOperationsAssignment_2_1_0(), "rule__EqualityExpression__OperationsAssignment_2_1_0");
					put(grammarAccess.getEqualityExpressionAccess().getParametersAssignment_2_1_1(), "rule__EqualityExpression__ParametersAssignment_2_1_1");
					put(grammarAccess.getAdditiveExpressionAccess().getParametersAssignment_1(), "rule__AdditiveExpression__ParametersAssignment_1");
					put(grammarAccess.getAdditiveExpressionAccess().getParametersAssignment_2_0_1(), "rule__AdditiveExpression__ParametersAssignment_2_0_1");
					put(grammarAccess.getAdditiveExpressionAccess().getParametersAssignment_2_1_1(), "rule__AdditiveExpression__ParametersAssignment_2_1_1");
					put(grammarAccess.getMultiplicativeExpressionAccess().getParametersAssignment_1(), "rule__MultiplicativeExpression__ParametersAssignment_1");
					put(grammarAccess.getMultiplicativeExpressionAccess().getOperationsAssignment_2_0_0(), "rule__MultiplicativeExpression__OperationsAssignment_2_0_0");
					put(grammarAccess.getMultiplicativeExpressionAccess().getParametersAssignment_2_0_1(), "rule__MultiplicativeExpression__ParametersAssignment_2_0_1");
					put(grammarAccess.getMultiplicativeExpressionAccess().getOperationsAssignment_2_1_0(), "rule__MultiplicativeExpression__OperationsAssignment_2_1_0");
					put(grammarAccess.getMultiplicativeExpressionAccess().getParametersAssignment_2_1_1(), "rule__MultiplicativeExpression__ParametersAssignment_2_1_1");
					put(grammarAccess.getUnaryExpressionAccess().getOperationsAssignment_0_1(), "rule__UnaryExpression__OperationsAssignment_0_1");
					put(grammarAccess.getUnaryExpressionAccess().getParametersAssignment_0_2(), "rule__UnaryExpression__ParametersAssignment_0_2");
					put(grammarAccess.getPrimaryExpressionAccess().getParametersAssignment_0_1(), "rule__PrimaryExpression__ParametersAssignment_0_1");
					put(grammarAccess.getPrimaryExpressionAccess().getParametersAssignment_1_1(), "rule__PrimaryExpression__ParametersAssignment_1_1");
					put(grammarAccess.getFunctionCallAccess().getNameAssignment_0(), "rule__FunctionCall__NameAssignment_0");
					put(grammarAccess.getFunctionCallAccess().getOperandsAssignment_2(), "rule__FunctionCall__OperandsAssignment_2");
					put(grammarAccess.getFunctionOperandsAccess().getParametersAssignment_1(), "rule__FunctionOperands__ParametersAssignment_1");
					put(grammarAccess.getFunctionOperandsAccess().getParametersAssignment_2_1(), "rule__FunctionOperands__ParametersAssignment_2_1");
					put(grammarAccess.getLiteralAccess().getValueStringAssignment_0_1(), "rule__Literal__ValueStringAssignment_0_1");
					put(grammarAccess.getLiteralAccess().getValueStringAssignment_1_1(), "rule__Literal__ValueStringAssignment_1_1");
					put(grammarAccess.getLiteralAccess().getOperationsAssignment_2_1(), "rule__Literal__OperationsAssignment_2_1");
					put(grammarAccess.getLiteralAccess().getValueStringAssignment_2_3(), "rule__Literal__ValueStringAssignment_2_3");
					put(grammarAccess.getLiteralAccess().getParametersAssignment_2_5(), "rule__Literal__ParametersAssignment_2_5");
					put(grammarAccess.getLiteralAccess().getElementsAssignment_4_1(), "rule__Literal__ElementsAssignment_4_1");
					put(grammarAccess.getLiteralAccess().getElementsAssignment_5_1(), "rule__Literal__ElementsAssignment_5_1");
					put(grammarAccess.getLiteralAccess().getExpressionTypeAssignment_6_1(), "rule__Literal__ExpressionTypeAssignment_6_1");
					put(grammarAccess.getLiteralAccess().getParametersAssignment_6_2(), "rule__Literal__ParametersAssignment_6_2");
					put(grammarAccess.getLiteralAccess().getParametersAssignment_6_3_1(), "rule__Literal__ParametersAssignment_6_3_1");
					put(grammarAccess.getLiteralAccess().getElementsAssignment_7_1(), "rule__Literal__ElementsAssignment_7_1");
					put(grammarAccess.getLiteralAccess().getElementsAssignment_8_1(), "rule__Literal__ElementsAssignment_8_1");
					put(grammarAccess.getLiteralAccess().getElementsAssignment_9_1(), "rule__Literal__ElementsAssignment_9_1");
					put(grammarAccess.getLiteralAccess().getElementsAssignment_10_1(), "rule__Literal__ElementsAssignment_10_1");
				}
			};
		}
		return nameMappings.get(element);
	}
	
	@Override
	protected Collection<FollowElement> getFollowElements(AbstractInternalContentAssistParser parser) {
		try {
			com.dexels.navajo.dsl.expression.ui.contentassist.antlr.internal.InternalNavajoExpressionParser typedParser = (com.dexels.navajo.dsl.expression.ui.contentassist.antlr.internal.InternalNavajoExpressionParser) parser;
			typedParser.entryRuleTopLevel();
			return typedParser.getFollowElements();
		} catch(RecognitionException ex) {
			throw new RuntimeException(ex);
		}		
	}
	
	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}
	
	public NavajoExpressionGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(NavajoExpressionGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
