package bootcamp.casacodigo.validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OneExistsValidator implements
	ConstraintValidator<OneExists, Object>{
	
	private String column;
	private Class<?> target;
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public void initialize(OneExists constraint) {
		this.column = constraint.column();
		this.target = constraint.target();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		String q = String.format("SELECT 1 FROM %s WHERE TRIM(LOWER(%s))=TRIM(LOWER(:value))",
				target.getName(), column);
		
		Query query = manager.createQuery(q);
		query.setParameter("value", value);
		List<?> res = query.getResultList();
		
		return !res.isEmpty();
	}
}
