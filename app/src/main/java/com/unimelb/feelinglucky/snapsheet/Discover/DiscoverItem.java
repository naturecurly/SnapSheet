package com.unimelb.feelinglucky.snapsheet.Discover;

import com.unimelb.feelinglucky.snapsheet.Story.Story;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by yuhaoliu on 7/09/16.
 */
@Entity(active = true)
public class DiscoverItem implements DiscoverItemInterface<DiscoverItem> {
    @Id(autoincrement = true)
    Long id;
    int clickCount;
    String coverURL;
    @Convert(columnType = String.class, converter = Story.StringConverter.class)
    List<String> contentURLs;
    /** Used for active entity operations. */
    @Generated(hash = 151651718)
    private transient DiscoverItemDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 127170706)
    public DiscoverItem(Long id, int clickCount, String coverURL, List<String> contentURLs) {
        this.id = id;
        this.clickCount = clickCount;
        this.coverURL = coverURL;
        this.contentURLs = contentURLs;
    }

    @Generated(hash = 1574487284)
    public DiscoverItem() {
    }

    @Override
    public String getCoverURL() {
        return this.coverURL;
    }

    @Override
    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    @Override
    public List<String> getContentURLs() {
        return this.contentURLs;
    }

    @Override
    public void setContentURLs(List<String> contentURLs) {
        this.contentURLs = contentURLs;
    }

    @Override
    public int getclickCount() {
        return clickCount;
    }

    public int getClickCount() {
        return this.clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    @Override
    public int compareTo(DiscoverItem rhs) {
        int result = 0;
        if (this.clickCount > rhs.clickCount){
            result = -1;
        }else if(this.clickCount < rhs.clickCount){
            result = 1;
        }
        return result;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1486821711)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDiscoverItemDao() : null;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
