package orj.worf.key;

public abstract interface Database
{
  public static final String dataSource = "dataSource";
  public static final String jdbcTemplate = "jdbcTemplate";
  public static final String transactionManager = "transactionManager";
  public static final String ehcacheManager = "cacheManager";
  public static final String simpleSqlSession = "simpleSqlSession";
  public static final String reuseSqlSession = "reuseSqlSession";
  public static final String batchSqlSession = "batchSqlSession";
  public static final int tx = 200;
  public static final int myTx = 195;
  public static final int cache = 180;
  public static final int exception = 160;
  public static final int serviceLog = 150;
}

