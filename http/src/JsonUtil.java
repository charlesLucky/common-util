import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

public class JsonUtil {

    private static final Logger log = Logger.getLogger(JsonUtil.class);
    private static final ObjectMapper MAPPER;

    static {
        //MAPPER = generateMapper(Inclusion.ALWAYS);
        MAPPER = generateMapper(Inclusion.NON_NULL);
    }

    private JsonUtil() {
    }

    /**
     * * ��jsonͨ������ת���ɶ���
     * * <pre> * {@link JsonUtil JsonUtil}.fromJson("{\"username\":\"username\", \"password\":\"password\"}", User.class);
     * * </pre> * 
     * @param <T>
     * @param json json�ַ��� 
     * @param clazz �������� 
     * @return ���ض��� 
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return clazz.equals(String.class) ? (T) json : MAPPER.readValue(json, clazz);
        } catch (IOException ex) {
            log.error(json + " �޷�ת��Ϊ " + clazz.getName() + " ����!", ex);
        }
        return null;
    }

    /**
     * * ��jsonͨ������ת���ɶ���
     * * <pre> * {@link JsonUtil JsonUtil}.fromJson("[{\"username\":\"username\", \"password\":\"password\"}, {\"username\":\"username\", \"password\":\"password\"}]", new TypeReference&lt;List&lt;User&gt;&gt;);
     * * </pre> * 
     * @param <T>
     * @param json json�ַ��� 
     * @param typeReference ��������
     * @return ���ض��� 
     */
    public static <T> T fromJson(String json, TypeReference<?> typeReference) {
        try {
            return (T) (typeReference.getType().equals(String.class) ? json : MAPPER.readValue(json, typeReference));
        } catch (IOException ex) {
            log.error(json + " �޷�ת��Ϊ " + typeReference.getClass().getName() + " ����!", ex);
        }
        return null;
    }

    /**
     * * ������ת����json
     * * <pre> * {@link JsonUtil JsonUtil}.toJson(user); * </pre> * 
     * @param <T>
     * @param src ���� 
     * @return ����json�ַ���
     */
    public static <T> String toJson(T src) {
        try {
            return src instanceof String ? (String) src : MAPPER.writeValueAsString(src);
        } catch (IOException ex) {
            log.error("Ŀ����� " + src.getClass().getName() + " ת�� JSON �ַ���ʱ�������쳣��", ex);
        }
        return null;
    }

    /**
     * * ������ת����json, ���������������
     * * <pre> * {@link JsonUtil JsonUtil}.toJson(user, {@link Inclusion Inclusion.ALWAYS});
     * * </pre> * * {@link Inclusion Inclusion ����ö��} * <ul> *
     * <li>{@link Inclusion Inclusion.ALWAYS ȫ������}</li> *
     * <li>{@link Inclusion Inclusion.NON_DEFAULT �ֶκͶ���Ĭ��ֵ��ͬ��ʱ�򲻻�����}</li> *
     * <li>{@link Inclusion Inclusion.NON_EMPTY �ֶ�ΪNULL����""��ʱ�򲻻�����}</li> *
     * <li>{@link Inclusion Inclusion.NON_NULL �ֶ�ΪNULLʱ�򲻻�����}</li> * </ul> * *
     * @param <T>
     * @param src ���� 
     * @param inclusion ����һ��ö��ֵ, ����������� 
     * @return ����json�ַ��� *
     */
    public static <T> String toJson(T src, Inclusion inclusion) {
        if (src instanceof String) {
            return (String) src;
        } else {
            ObjectMapper customMapper = generateMapper(inclusion);
            try {
                return customMapper.writeValueAsString(src);
            } catch (IOException ex) {
                log.error("Ŀ����� " + src.getClass().getName() + " ת�� JSON �ַ���ʱ�������쳣��", ex);
            }
        }
        return null;
    }

    /**
     * * ������ת����json, �������ö���
     * * <pre> * {@link ObjectMapper ObjectMapper} mapper = new ObjectMapper(); 
     * mapper.setSerializationInclusion({@link Inclusion Inclusion.ALWAYS}); 
     * mapper.configure({@link Feature Feature.FAIL_ON_UNKNOWN_PROPERTIES}, false); 
     * mapper.configure({@link Feature Feature.FAIL_ON_NUMBERS_FOR_ENUMS}, true); 
     * mapper.setDateFormat(new {@link SimpleDateFormat SimpleDateFormat}("yyyy-MM-dd HH:mm:ss")); 
     * {@link JsonUtil JsonUtil}.toJson(user, mapper);
     * * </pre> * * {@link ObjectMapper ObjectMapper} * * @see ObjectMapper * *
     * @param <T>
     * @param src ���� 
     * @param mapper ���ö���
     * @return ����json�ַ��� 
     */
    public static <T> String toJson(T src, ObjectMapper mapper) {
        if (null != mapper) {
            if (src instanceof String) {
                return (String) src;
            } else {
                try {
                    return mapper.writeValueAsString(src);
                } catch (IOException ex) {
                    log.error("Ŀ����� " + src.getClass().getName() + " ת�� JSON �ַ���ʱ�������쳣��", ex);
                }
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * * ����{@link ObjectMapper ObjectMapper}����, ���ڶ����ԵĲ��� * 
     * @return
     * {@link ObjectMapper ObjectMapper}����
     */
    public static ObjectMapper mapper() {
        return MAPPER;
    }

    /**
     * * ͨ��Inclusion����ObjectMapper���� * * {@link Inclusion Inclusion ����ö��} * <ul>
     * * <li>{@link Inclusion Inclusion.ALWAYS ȫ������}</li> *
     * <li>{@link Inclusion Inclusion.NON_DEFAULT �ֶκͶ���Ĭ��ֵ��ͬ��ʱ�򲻻�����}</li> *
     * <li>{@link Inclusion Inclusion.NON_EMPTY �ֶ�ΪNULL����""��ʱ�򲻻�����}</li> *
     * <li>{@link Inclusion Inclusion.NON_NULL �ֶ�ΪNULLʱ�򲻻�����}</li> * </ul> * *
     * @param inclusion ����һ��ö��ֵ, ����������� 
     * @return ����ObjectMapper����
     */
    private static ObjectMapper generateMapper(Inclusion inclusion) {
        ObjectMapper customMapper = new ObjectMapper(); // �������ʱ�������Եķ�� 
        //customMapper.setSerializationInclusion(inclusion); // ��������ʱ������JSON�ַ����д��ڵ�Java����ʵ��û�е����� 
        customMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false); // ��ֹʹ��int����Enum��order()�����л�Enum,�ǳ�Σ�U 
        customMapper.configure(Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true); // �������ڸ�ʽ��ͳһΪ������ʽ 
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); 
        return customMapper; 
    } 
    public static void main(String[] args) {
    	User user1 = new User("����1", "18", "��");
		User user2 = new User("����2", "19", "Ů");
		User user3 = new User("ʯͷ", "20", "Ů");
		List<User> users = new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		System.out.println(JsonUtil.toJson(users));
		String str="[{\"name\":\"����1\",\"age\":\"18\",\"gender\":\"��\"},{\"name\":\"����2\",\"age\":\"19\",\"gender\":\"Ů\"}]";
		users=JsonUtil.fromJson(str, users.getClass());
		System.out.println(users.get(0));
    }
}
