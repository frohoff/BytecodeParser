/**
 * 
 */
package bclibs.analysis.decoders;

import static bclibs.analysis.Opcodes.StackElementLength.DOUBLE;
import static bclibs.analysis.Opcodes.StackElementLength.ONE;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Descriptor;
import bclibs.analysis.Opcodes.StackElementLength;
import bclibs.analysis.opcodes.MethodInvocationOpcode;

public class DecodedMethodInvocationOp extends DecodedOp {
	protected int nbParameters;
	protected String descriptor;
	protected String name;
	protected StackElementLength[] pops;
	protected StackElementLength[] pushes;
	
	public DecodedMethodInvocationOp(MethodInvocationOpcode mop, CtBehavior behavior, CodeIterator iterator, int index) throws NotFoundException {
		super(mop, behavior, iterator, index);
		descriptor = behavior.getMethodInfo().getConstPool().getMethodrefType(getMethodRefIndex());
		name = behavior.getMethodInfo().getConstPool().getMethodrefName(getMethodRefIndex());
		ClassPool cp = behavior.getDeclaringClass().getClassPool();
		CtClass[] methodParameterTypes = Descriptor.getParameterTypes(descriptor, cp);
		nbParameters = methodParameterTypes.length;
		StackElementLength[] pops = new StackElementLength[methodParameterTypes.length + (mop.isInstanceMethod() ? 1 : 0)];
		for(int i = methodParameterTypes.length - 1, j = 0; i >= 0; i--, j++) {
			CtClass ctClass = methodParameterTypes[i];
			if(ctClass.isPrimitive()) {
				char d = ((CtPrimitiveType) ctClass).getDescriptor();
				if(d == 'J' || d == 'D') {
					pops[j] = DOUBLE;
				} else {
					pops[j] = ONE;
				}
			}
		}
		if(mop.isInstanceMethod())
			pops[pops.length - 1] = ONE;
		this.pops = pops;
		CtClass returnType = Descriptor.getReturnType(descriptor, cp);
		StackElementLength returnTypeLength = ONE;
		if(returnType.isPrimitive()) {
			char d = ((CtPrimitiveType) returnType).getDescriptor();
			if(d == 'V') {
				returnTypeLength = null;
			}
			if(d == 'J' || d == 'D') {
				returnTypeLength = DOUBLE;
			}
		}
		pushes = returnTypeLength != null ? new StackElementLength[] { returnTypeLength } : new StackElementLength[0];
	}
	
	public int getMethodRefIndex() {
		return parameterValues[0];
	}
	public String getDescriptor() {
		return descriptor;
	}
	public String getName() {
		return name;
	}
	public int getNbParameters() {
		return nbParameters;
	}
	public StackElementLength[] getPops() {
		return pops;
	}
	public StackElementLength[] getPushes() {
		return pushes;
	}
}