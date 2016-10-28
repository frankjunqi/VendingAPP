package de.mindpipe.android.logging.log4j;

import java.io.IOException;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;

public class LogConfigurator {
    private String fileName;
    private String filePattern;
    private boolean immediateFlush;
    private boolean internalDebugging;
    private String logCatPattern;
    private int maxBackupSize;
    private long maxFileSize;
    private boolean resetConfiguration;
    private Level rootLevel;
    private boolean useFileAppender;
    private boolean useLogCatAppender;

    public LogConfigurator() {
        this.rootLevel = Level.DEBUG;
        this.filePattern = "%d - [%p::%c::%C] - %m%n";
        this.logCatPattern = "%m%n";
        this.fileName = "android-log4j.log";
        this.maxBackupSize = 5;
        this.maxFileSize = 524288;
        this.immediateFlush = true;
        this.useLogCatAppender = true;
        this.useFileAppender = true;
        this.resetConfiguration = true;
        this.internalDebugging = false;
    }

    public LogConfigurator(String fileName) {
        this.rootLevel = Level.DEBUG;
        this.filePattern = "%d - [%p::%c::%C] - %m%n";
        this.logCatPattern = "%m%n";
        this.fileName = "android-log4j.log";
        this.maxBackupSize = 5;
        this.maxFileSize = 524288;
        this.immediateFlush = true;
        this.useLogCatAppender = true;
        this.useFileAppender = true;
        this.resetConfiguration = true;
        this.internalDebugging = false;
        setFileName(fileName);
    }

    public LogConfigurator(String fileName, Level rootLevel) {
        this(fileName);
        setRootLevel(rootLevel);
    }

    public LogConfigurator(String fileName, Level rootLevel, String filePattern) {
        this(fileName);
        setRootLevel(rootLevel);
        setFilePattern(filePattern);
    }

    public LogConfigurator(String fileName, int maxBackupSize, long maxFileSize, String filePattern, Level rootLevel) {
        this(fileName, rootLevel, filePattern);
        setMaxBackupSize(maxBackupSize);
        setMaxFileSize(maxFileSize);
    }

    public void configure() {
        Logger root = Logger.getRootLogger();
        if (isResetConfiguration()) {
            LogManager.getLoggerRepository().resetConfiguration();
        }
        LogLog.setInternalDebugging(isInternalDebugging());
        if (isUseFileAppender()) {
            configureFileAppender();
        }
        if (isUseLogCatAppender()) {
            configureLogCatAppender();
        }
        root.setLevel(getRootLevel());
    }

    public void setLevel(String loggerName, Level level) {
        Logger.getLogger(loggerName).setLevel(level);
    }

    private void configureFileAppender() {
        Logger root = Logger.getRootLogger();
        try {
            RollingFileAppender rollingFileAppender = new RollingFileAppender(new PatternLayout(getFilePattern()), getFileName());
            rollingFileAppender.setMaxBackupIndex(getMaxBackupSize());
            rollingFileAppender.setMaximumFileSize(getMaxFileSize());
            rollingFileAppender.setImmediateFlush(isImmediateFlush());
            root.addAppender(rollingFileAppender);
        } catch (IOException e) {
            throw new RuntimeException("Exception configuring log system", e);
        }
    }

    private void configureLogCatAppender() {
        Logger.getRootLogger().addAppender(new LogCatAppender(new PatternLayout(getLogCatPattern())));
    }

    public Level getRootLevel() {
        return this.rootLevel;
    }

    public void setRootLevel(Level level) {
        this.rootLevel = level;
    }

    public String getFilePattern() {
        return this.filePattern;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    public String getLogCatPattern() {
        return this.logCatPattern;
    }

    public void setLogCatPattern(String logCatPattern) {
        this.logCatPattern = logCatPattern;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getMaxBackupSize() {
        return this.maxBackupSize;
    }

    public void setMaxBackupSize(int maxBackupSize) {
        this.maxBackupSize = maxBackupSize;
    }

    public long getMaxFileSize() {
        return this.maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public boolean isImmediateFlush() {
        return this.immediateFlush;
    }

    public void setImmediateFlush(boolean immediateFlush) {
        this.immediateFlush = immediateFlush;
    }

    public boolean isUseFileAppender() {
        return this.useFileAppender;
    }

    public void setUseFileAppender(boolean useFileAppender) {
        this.useFileAppender = useFileAppender;
    }

    public boolean isUseLogCatAppender() {
        return this.useLogCatAppender;
    }

    public void setUseLogCatAppender(boolean useLogCatAppender) {
        this.useLogCatAppender = useLogCatAppender;
    }

    public void setResetConfiguration(boolean resetConfiguration) {
        this.resetConfiguration = resetConfiguration;
    }

    public boolean isResetConfiguration() {
        return this.resetConfiguration;
    }

    public void setInternalDebugging(boolean internalDebugging) {
        this.internalDebugging = internalDebugging;
    }

    public boolean isInternalDebugging() {
        return this.internalDebugging;
    }
}
