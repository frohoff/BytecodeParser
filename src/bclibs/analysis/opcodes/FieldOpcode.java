/**
 * 
 */
package bclibs.analysis.opcodes;

import static bclibs.analysis.Opcodes.OpParameterType.U2;
import javassist.CtBehavior;
import javassist.bytecode.CodeIterator;
import bclibs.analysis.Opcodes.StackElementLength;
import bclibs.analysis.decoders.DecodedFieldOp;

public class FieldOpcode extends BasicOpcode {
	private final DecodedFieldOp decodedOp;
	
	public FieldOpcode(int code) {
		this(code, null);
	}
	private FieldOpcode(int code, DecodedFieldOp decodedOp) {
		super(code, U2);
		this.decodedOp = decodedOp;
	}
	@Override
	public FieldOpcode init(CtBehavior behavior, CodeIterator iterator, int index) {
		return new FieldOpcode(code, decode(behavior, iterator, index));
	}
	@Override
	public DecodedFieldOp decode(CtBehavior behavior, CodeIterator iterator, int index) {
		if(decodedOp != null)
			return decodedOp;
		try {
			DecodedFieldOp decodedOp = new DecodedFieldOp(this, behavior, iterator, index);
			return decodedOp;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public StackElementLength[] getPops() {
		if(decodedOp == null)
			throw new IllegalStateException("must be initialized before !");
		return decodedOp.getPops();
	}
	@Override
	public StackElementLength[] getPushes() {
		if(decodedOp == null)
			throw new IllegalStateException("must be initialized before !");
		return decodedOp.getPushes();
	}
}