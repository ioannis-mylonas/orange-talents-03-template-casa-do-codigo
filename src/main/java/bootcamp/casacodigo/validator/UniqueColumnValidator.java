package bootcamp.casacodigo.validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.Assert;

public class UniqueColumnValidator implements
	ConstraintValidator<UniqueColumn, Object>{
	
	private Class<?> target;
	private String column;
	@PersistenceContext
	private EntityManager manager;

	@Override
	public void initialize(UniqueColumn constraint) {
		target = constraint.target();
		column = constraint.column();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) return true;

		String q = String.format("SELECT 1 FROM %s WHERE TRIM(LOWER(%s))=TRIM(LOWER(:value))", target.getName(), column);
		Query query = manager.createQuery(q);
		query.setParameter("value", value);
		
		List<?> result = query.getResultList();
		Assert.state(result.size() <= 1, String.format("Banco de dados contém mais de um %s que deveria ser único. Tabela: %s.", column, target.getName()));
		
		return result.isEmpty();
	}

}
