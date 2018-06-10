/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.script.dom.model;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.views.tree.ITreeModelView;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.script.dom.IArchiScriptDOMFactory;

/**
 * Current model object
 * 
 * Represents the current model
 * This can be the selected model in focus if run in the UI, or the current model if run from the ACLI
 * It can be loaded from file, or created anew.
 * 
 * @author Phillip Beauvoir
 */
public class Model implements IArchiScriptDOMFactory {
    
    public Object getDOMroot() {
        // Default is null, no current model
        IArchimateModel currentModel = null;
        
        // Get and wrap the currently selected model in the UI if there is one
        if(PlatformUI.isWorkbenchRunning()) {
            IWorkbenchPart activePart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
            
            // Fallback to tree
            if(!(activePart instanceof ITreeModelView || activePart instanceof IDiagramModelEditor)) {
                activePart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ITreeModelView.ID);
            }
            
            if(activePart != null) {
                currentModel = activePart.getAdapter(IArchimateModel.class);
            }
        }
        // Else, if we are running in CLI mode, get the Current Model if there is one
        else {
            currentModel = CommandLineState.getModel();
        }
        
        return currentModel == null ? null : new ArchimateModelProxy(currentModel);
    }
}
