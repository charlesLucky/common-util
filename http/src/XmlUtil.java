import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlUtil {
	public static Object xml2Bean(String result, String className) {
		try {
			Document document = DocumentHelper.parseText(result);
			Element root = document.getRootElement();
			Class c = Class.forName(className);
			Field[] properties = c.getDeclaredFields();// ���ʵ��������
			Object obj = c.newInstance();// ��ö�����µ�ʵ��
			for (Field field : properties) {
				String methName = field.getName();
				Method setmeth = obj.getClass().getMethod(method_set(methName),
						field.getType());
				String name = field.getType().getName();
				String value = ((Element) root).elementText(methName);
				if (!Common.isEmpty(value)) {
					value = value.trim();
					if (name.contains("int") || name.contains("Integer")) {
						setmeth.invoke(obj,
								new Object[] { Integer.parseInt(value) });
					} else if (name.contains("double")
							|| name.contains("Double")) {
						setmeth.invoke(obj,
								new Object[] { Double.parseDouble(value) });
					} else if (name.contains("String")) {
						setmeth.invoke(obj, new Object[] { value });
					} else if (name.contains("Long") || name.contains("long")) {
						setmeth.invoke(obj,
								new Object[] { Long.parseLong(value) });
					} else if (name.contains("Date")) {
						setmeth.invoke(obj, new Object[] { Date.parse(value) });
					}
				}
			}
			return obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String method_get(String methodName) {
		return "get" + methodName.substring(0, 1).toUpperCase()
				+ methodName.substring(1);
	}

	public static String method_set(String methodName) {
		return "set" + methodName.substring(0, 1).toUpperCase()
				+ methodName.substring(1);
	}

	public static <T> void beans2XmlFile(Class cls, List<T> entityPropertys,
			String Encode, String XMLPathAndName) {
		long lasting = System.currentTimeMillis();// Ч�ʼ��
		try {
			XMLWriter writer = null;// ����дXML�Ķ���
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(Encode);// ����XML�ļ��ı����ʽ

			String filePath = XMLPathAndName;// ����ļ���ַ
			File file = new File(filePath);// ����ļ�
			if (file.exists()) {
				file.delete();
			}
			// �½�student.xml�ļ�����������
			Document document = DocumentHelper.createDocument();
			String rootname = cls.getSimpleName();// �������
			Element root = document.addElement(rootname + "s");// ��Ӹ��ڵ�
			Field[] properties = cls.getDeclaredFields();// ���ʵ�������������

			for (T t : entityPropertys) { // �ݹ�ʵ��
				Element secondRoot = root.addElement(rootname); // �����ڵ�

				for (int i = 0; i < properties.length; i++) {
					// ����get����
					Method meth = t.getClass().getMethod(
							"get"
									+ properties[i].getName().substring(0, 1)
											.toUpperCase()
									+ properties[i].getName().substring(1));
					// Ϊ�����ڵ�������ԣ�����ֵΪ��Ӧ���Ե�ֵ
					secondRoot.addElement(properties[i].getName()).setText(
							meth.invoke(t).toString());

				}
			}
			// ����XML�ļ�
			writer = new XMLWriter(new FileWriter(file), format);
			writer.write(document);
			writer.close();
			long lasting2 = System.currentTimeMillis();
			System.out.println("д��XML�ļ�����,��ʱ" + (lasting2 - lasting) + "ms");
		} catch (Exception e) {
			System.out.println("XML�ļ�д��ʧ��");
		}

	}

	public static <T> String beans2XmlStr(Class cls, List<T> entityPropertys) {
		long lasting = System.currentTimeMillis();// Ч�ʼ��
		Document document = null;
		try {

			// �½�student.xml�ļ�����������
			document = DocumentHelper.createDocument();
			String rootname = cls.getSimpleName();// �������
			Element root = document.addElement(rootname + "s");// ��Ӹ��ڵ�
			Field[] properties = cls.getDeclaredFields();// ���ʵ�������������

			for (T t : entityPropertys) { // �ݹ�ʵ��
				Element secondRoot = root.addElement(rootname); // �����ڵ�

				for (int i = 0; i < properties.length; i++) {
					// ����get����
					Method meth = t.getClass().getMethod(
							"get"
									+ properties[i].getName().substring(0, 1)
											.toUpperCase()
									+ properties[i].getName().substring(1));
					// Ϊ�����ڵ�������ԣ�����ֵΪ��Ӧ���Ե�ֵ
					secondRoot.addElement(properties[i].getName()).setText(
							meth.invoke(t).toString());

				}
			}

			long lasting2 = System.currentTimeMillis();
			System.out.println("д��XML�ļ�����,��ʱ" + (lasting2 - lasting) + "ms");
		} catch (Exception e) {
			System.out.println("XML�ļ�д��ʧ��");
		}
		return document.asXML();
	}

	// ��ȡһ��xml������bean
	@SuppressWarnings("unchecked")
	public static <T> List<T> xml2Beans(String XMLStr, Class cls) {
		long lasting = System.currentTimeMillis();// Ч�ʼ��
		List<T> list = new ArrayList<T>();// ����list����
		try {
			Document doc = DocumentHelper.parseText(XMLStr);// dom4j��ȡ
			Element root = doc.getRootElement();// ��ø��ڵ�
			Element foo;// �����ڵ�
			Field[] properties = cls.getDeclaredFields();// ���ʵ��������
			// ʵ����get����
			Method getmeth;
			// ʵ����set����
			Method setmeth;

			for (Iterator i = root.elementIterator(cls.getSimpleName()); i
					.hasNext();) {// ����t.getClass().getSimpleName()�ڵ�
				foo = (Element) i.next();// ��һ�������ڵ�

				T t = (T) cls.newInstance();// ��ö�����µ�ʵ��

				for (int j = 0; j < properties.length; j++) {// �����������ӽڵ�

					// ʵ����set����
					setmeth = cls.getMethod("set"
							+ properties[j].getName().substring(0, 1)
									.toUpperCase()
							+ properties[j].getName().substring(1),
							properties[j].getType());
					// properties[j].getType()Ϊset������ڲ����Ĳ�������(Class����)
					setmeth.invoke(t, foo.elementText(properties[j].getName()));// ����Ӧ�ڵ��ֵ����

				}
				list.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long lasting2 = System.currentTimeMillis();
		System.out.println("����XML�ļ�����,��ʱ" + (lasting2 - lasting) + "ms");
		return list;
	}

	public static void main(String[] args) {
		User user = new User();
		User user1 = new User("����1", "18", "��");
		User user2 = new User("����2", "19", "Ů");
		User user3 = new User("ʯͷ", "20", "Ů");
		List<User> users = new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		System.out.println(XmlUtil.beans2XmlStr(User.class, users));
	}

}
