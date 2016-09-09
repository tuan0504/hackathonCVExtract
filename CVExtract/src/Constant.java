public  class Constant {
	
	public final static  int PERSONAL_INDEX  = 0;
	public final static  int EDUCATION_INDEX  = 1;
	public final static  int EMPLOYMENT_INDEX  = 2;
	
	public static  String SPLIT_PARAM  = "[\\n\\r]+";
	public static  String SPLIT_PERSONAL  = ":";
	public static  String SPLIT_YEAR  = " ";
	
	public static  String SPECIAL_INFO = "(co\\.,)|(co\\.)";
	
	public static  String OUT_TEXT_PARAGRAPH  = "[^(a-zA-Z0-9)(" + VNValue.NATIONAL_CHAR_AND 
										+ ")(" + SPLIT_PERSONAL 
										+ ")(" + SPECIAL_INFO + ")( )]";
	
	public static  String OUT_TEXT_EMAIL  = "[^((a-zA-Z0-9\\.)+@(a-zA-Z0-9)+\\.(a-zA-Z0-9\\.)+))]";
	
	public static  String YEAR_VALUE  = "[^\\d(/)]";
	
	public static  String PERSONAL  = ".*((profile)|(thông tin)).*";
	public static  String EDUCATION  = ".*((edu)|(học vấn)).*";
	public static  String EMPLOYMENT  = ".*((experience)|(làm việc)).*";
	
	public static  String OBJECTIVE  = "objective";
	public static  String SKILL  = "skill";
	
/***********PERSONAL***********************/	
	public static  String NAME = "(.*)((name)|(tên))(.*)";
	public static  String SALUTATION = "salutation";
	public static  String GENDER = "(.*)((gender)|(giới))(.*)";
	public static  String DOB = "(.*)(((d+)(.*)(o+)(.*)(birth)+)|(ngày sinh))(.*)";
	public static  String NATIONALITY = "(.*)((nationality)|(quốc tịch))(.*)";
	public static  String ADDRESS = ".*(((city|ward|street)+)|(địa chỉ)).*";
	public static  String COUNTRY = ".*country.*";
	public static  String EMAIL = ".*(email|e-mail)+.*";
	public static  String MOBILE = ".*((mobile)|(đtdđ)).*";
	public static  String POSITION_EXPECTED = "position expected";
	public static  String SALARY_EXPECTED = "salary expected";
	public static  String LOCATION_EXPECTED = "location expected";
	public static  String MATITAL_STATUS = ".*(((m.*status)+)|(hôn nhân)).*";
	
/***********EDUCATION***********************/
	public static  String YEAR = ".*(\\d{4}).*([^\\d])*";
	public static  String DEGREE = ".*((bachelor|doctor|master)|(ngành)).*";
	public static  String SCHOOL = ".*((school|university|college)|(đh|(đại học)|cđ|(cao đẳng)|tc|(trung cấp)|(trường)|(trung tâm))).*";
	public static  String LOCATION = "location";
	
/***********EMPLOY***********************/
	public static  String POSITION = ".*(("+ ENValue.CARRER_CHAR +")|("+ VNValue.CARRER_CHAR +")).*";
	public static  String EMPLOYER = ".*(("+ ENValue.COMPANY_CHAR +")|("+ VNValue.COMPANY_CHAR +")).*";
	public static  String LOCATION_E = "location_expected";
	
/***********COMMON***********************/	
	public static  String NUMBER_DATE = "[^0-9/\\-]";
	
}