/**
 * 
 */
package bclibs.analysis.decoders;

import static bclibs.analysis.Opcodes.StackElementLength.DOUBLE;
import static bclibs.analysis.Opcodes.StackElementLength.ONE;
import javassist.CtBehavior;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Descriptor;
import javassist.bytecode.Opcode;
import bclibs.analysis.Opcodes.StackElementLength;
import bclibs.analysis.opcodes.FieldOpcode;

public class DecodedFieldOp extends DecodedOp {
	protected String descriptor;
	protected boolean load;
	protected StackElementLength stackElementLength;
	
	public DecodedFieldOp(FieldOpcode fo, CtBehavior behavior, CodeIterator iterator, int index) {
		super(fo, behavior, iterator, index);
		String descriptor = behavior.getMethodInfo().getConstPool().getFieldrefType(getMethodRefIndex());
		StackElementLength sel = ONE;
		if(Descriptor.dataSize(descriptor) == 2)
			sel = DOUBLE;
		this.stackElementLength = sel;
		this.descriptor = descriptor;
		this.load = fo.getCode() == Opcode.GETFIELD || fo.getCode() == Opcode.GETSTATIC;
	}
	
	public int getMethodRefIndex() {
		return parameterValues[0];
	}
	public String getDescriptor() {
		return descriptor;
	}
	public StackElementLength[] getPops() {
		if(!load)
			return new StackElementLength[] { stackElementLength };
		return new StackElementLength[0];
	}
	public StackElementLength[] getPushes() {
		if(load)
			return new StackElementLength[] { stackElementLength };
		return new StackElementLength[0];
	}
	public boolean isRead() {
		return load;
	}
}