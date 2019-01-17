package com.eastwood.tools.idea.declaration;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.android.facet.AndroidFacet;
import org.jetbrains.android.util.AndroidResourceUtil;
import org.jetbrains.annotations.Nullable;

public class AndroidGotoDeclarationHandler implements GotoDeclarationHandler {

    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
        if (!(sourceElement instanceof PsiIdentifier)) {
            return null;
        }

        PsiFile file = sourceElement.getContainingFile();
        if (file == null) {
            return null;
        }

        AndroidFacet facet = AndroidFacet.getInstance(file);
        if (facet == null) {
            return null;
        }

        PsiReferenceExpression refExp = PsiTreeUtil.getParentOfType(sourceElement, PsiReferenceExpression.class);
        if (refExp == null) {
            return null;
        }

        AndroidResourceUtil.MyReferredResourceFieldInfo info = GotoResourceHelper.getReferredResource(facet, refExp);
        if (info == null) {
            PsiElement parent = refExp.getParent();
            if (parent instanceof PsiReferenceExpression) {
                info = GotoResourceHelper.getReferredResource(facet, refExp);
            }

            if (info == null) {
                parent = parent.getParent();
                if (parent instanceof PsiReferenceExpression) {
                    info = GotoResourceHelper.getReferredResource(facet, refExp);
                }
            }
        }

        return info == null ? null : GotoResourceHelper.getGotoDeclarationTargets(facet, info, refExp);
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext) {
        return null;
    }

}