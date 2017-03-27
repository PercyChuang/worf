package orj.worf.aop.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcuts {
	@Pointcut("execution(public * *.*(..))")
	public void publicMethod() {
	}

	@Pointcut("execution(* *.*(..))")
	public void allMethod() {
	}

	@Pointcut("execution(* *.model..*(..))")
	public void allModel() {
	}

	@Pointcut("allMethod() and !allModel()")
	public void allMethodWithoutModel() {
	}

	@Pointcut("allMethod() and orj.worf.aop.SpringPointcuts.springTransactional()")
	public void allTransaction() {
	}
}