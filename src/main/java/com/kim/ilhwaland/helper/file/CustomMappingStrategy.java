package com.kim.ilhwaland.helper.file;



import com.opencsv.bean.BeanField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

/** csv : 사용자 정의 열 이름과 순서를 모두 적용 하기위한 설정 */
public class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
	
	@Override
	public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
		
		// 부모 메소드를 호출 하지않으면 해당 필드의 value 값이 출력되지 않는다.
		super.generateHeader(bean);
		
		final int numColumns  = getFieldMap().values().size();
		
		String[] header = new String[numColumns];
		
		BeanField<T, Integer> beanField;
		for (int i = 0; i < numColumns; i++) {
			 	// 주어진 열 위치에 대한 필드를 가져온다.
	            beanField = findField(i);
	            String columnHeaderName = extractHeaderName(beanField);
	            header[i] = columnHeaderName;  // 필드의 이름을 배열에 넣는다
	    }
		return header;
	}
	
	private String extractHeaderName(BeanField<T, Integer> beanField) {
		if (beanField == null || beanField.getField() == null || beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0) {
			return "";
	    }
		
		// @CsvBindByName은 여러개를 가질 수 있기 때문에, 특정 @CsvBindByName를 지정한다.
        CsvBindByName bindByNameAnnotation = beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0];
        return bindByNameAnnotation.column();
	}
}
