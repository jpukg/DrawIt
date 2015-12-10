package draw_it.db_unit;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

public class DataBaseFiller {
    private String fullXmlDataFileName;
    private DataSource dataSource;

    public DataBaseFiller(String fullXmlDataFileName, DataSource dataSource){
        this.fullXmlDataFileName = fullXmlDataFileName;
        this.dataSource = dataSource;
    }

    public void fill() throws Exception {
        Connection connect= null;
        try {
            connect = DataSourceUtils.getConnection(dataSource);
            IDatabaseConnection dbUnitConnect = new DatabaseConnection(connect);

            IDataSet xmlDataSet = new XmlDataSet(new FileInputStream(new File(fullXmlDataFileName)));

            DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnect, xmlDataSet);
        } finally {
            DataSourceUtils.releaseConnection(connect, dataSource);
        }
    }
}
