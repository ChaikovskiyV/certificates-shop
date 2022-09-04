package com.epam.esm.dao;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type Query storage.
 * <p>
 * This class includes a set of constants for building queries using HLQ
 */
public final class QueryStorage {
    /**
     * The constant AND.
     */
    public static final String AND = " AND ";
    /**
     * The constant LE.
     */
    public static final String LE = " <= ";
    /**
     * The constant BETWEEN.
     */
    public static final String BETWEEN = " BETWEEN ";
    /**
     * The constant ORDER_BY.
     */
    public static final String ORDER_BY = " ORDER BY ";

    //tag queries
    /**
     * The constant GET_ALL_TAGS.
     */
    public static final String GET_ALL_TAGS = "FROM Tag";
    /**
     * The constant FIND_TAG_BY_NAME.
     */
    public static final String FIND_TAG_BY_NAME = "FROM Tag WHERE name = :name";
    /**
     * The constant FIND_TAGS_BY_CERTIFICATE_ID.
     */
    public static final String FIND_TAGS_BY_CERTIFICATE_ID = "FROM Tag t JOIN FETCH t.certificates c WHERE c.id = :certificateId";
    /**
     * The constant FIND_MOST_WIDELY_USED_TAG_IN_USER_WITH_HIGHEST_COST_OF_ORDERS.
     */
    public static final String FIND_MOST_WIDELY_USED_TAG_IN_USER_WITH_HIGHEST_COST_OF_ORDERS =
            "SELECT t.* FROM tags AS t WHERE t.tag_name IN " +
                    "(SELECT t1.t2_name FROM " +
                    "(SELECT t2.tag_name as t2_name, count(t2.tag_name) AS count_t2 FROM tags AS t2 " +
                    "JOIN gift_certificates_tags AS c_t ON t2.id=c_t.tag_id " +
                    "JOIN gift_certificates AS c ON c_t.certificate_id=c.id " +
                    "JOIN orders_gift_certificates AS o_c ON c.id=o_c.certificate_id " +
                    "JOIN orders AS o ON o_c.order_id=o.id GROUP BY t2.tag_name, o.user_id HAVING o.user_id IN " +
                    "(SELECT user.u_id FROM " +
                    "(SELECT o2.user_id AS u_id, SUM(o2.cost) AS o2_cost FROM orders AS o2 GROUP BY u_id HAVING o2_cost = " +
                    "(SELECT MAX(sum_c.sum_cost) FROM " +
                    "(SELECT SUM(o3.cost) AS sum_cost FROM orders AS o3 GROUP BY o3.user_id) AS sum_c)) AS user) AND count_t2 = " +
                    "(SELECT MAX(t4.count_t3) FROM " +
                    "(SELECT t3.tag_name, COUNT(t3.tag_name) AS count_t3 FROM tags AS t3 " +
                    "JOIN gift_certificates_tags AS c_t1 ON t3.id=c_t1.tag_id " +
                    "JOIN gift_certificates AS c1 ON c_t1.certificate_id=c1.id " +
                    "JOIN orders_gift_certificates AS o_c1 ON c1.id=o_c1.certificate_id " +
                    "JOIN orders AS o1 ON o_c1.order_id=o1.id WHERE o1.user_id IN " +
                    "(SELECT user.u_id FROM " +
                    "(SELECT o2.user_id AS u_id, SUM(o2.cost) AS o2_cost FROM orders AS o2 GROUP BY u_id HAVING o2_cost = " +
                    "(SELECT MAX(sum_c.sum_cost) FROM " +
                    "(SELECT SUM(o3.cost) AS sum_cost FROM orders AS o3 GROUP BY o3.user_id) AS sum_c)) AS user) " +
                    "GROUP BY t3.tag_name) AS t4)) AS t1)";

    //certificate queries
    /**
     * The constant GET_ALL_CERTIFICATES.
     */
    public static final String GET_ALL_CERTIFICATES = "FROM GiftCertificate";
    /**
     * The constant FIND_CERTIFICATE_BY_NAME.
     */
    public static final String FIND_CERTIFICATE_BY_NAME = "FROM GiftCertificate WHERE name LIKE CONCAT('%', ?1, '%')";
    /**
     * The constant FIND_CERTIFICATE_BY_DESCRIPTION.
     */
    public static final String FIND_CERTIFICATE_BY_DESCRIPTION = "FROM GiftCertificate WHERE description LIKE CONCAT('%', ?1, '%')";
    /**
     * The constant FIND_CERTIFICATE_BY_TAG_NAME.
     */
    public static final String FIND_CERTIFICATE_BY_TAG_NAME = "FROM GiftCertificate g JOIN FETCH g.tags t WHERE t.name IN ";
    /**
     * The constant FIND_CERTIFICATE_BY_USER_ID.
     */
    public static final String FIND_CERTIFICATE_BY_USER_ID = "FROM GiftCertificate g JOIN FETCH g.orders o JOIN FETCH o.user u WHERE u.id=?1";

    //user queries
    /**
     * The constant GET_ALL_USERS.
     */
    public static final String GET_ALL_USERS = "FROM User";
    /**
     * The constant FIND_USER_BY_FIRST_NAME.
     */
    public static final String FIND_USER_BY_FIRST_NAME = "FROM User WHERE firstName LIKE CONCAT('%', ?1, '%')";
    /**
     * The constant FIND_USER_BY_LAST_NAME.
     */
    public static final String FIND_USER_BY_LAST_NAME = "FROM User WHERE lastName LIKE CONCAT('%', ?1, '%')";
    /**
     * The constant FIND_USER_BY_FIRST_NAME_AND_LAST_NAME.
     */
    public static final String FIND_USER_BY_FIRST_NAME_AND_LAST_NAME = "FROM User WHERE firstName LIKE CONCAT('%', ?1, '%') " +
            "AND lastName LIKE CONCAT('%', ?2, '%')";
    /**
     * The constant FIND_USER_BY_EMAIL.
     */
    public static final String FIND_USER_BY_EMAIL = "FROM User WHERE email=?1";
    /**
     * The constant FIND_USER_BY_CERTIFICATE_ID.
     */
    public static final String FIND_USER_BY_CERTIFICATE_ID = "FROM User u JOIN FETCH u.orders o JOIN FETCH o.certificates c WHERE c.id=?1";

    //order queries
    /**
     * The constant GET_ALL_ORDERS.
     */
    public static final String GET_ALL_ORDERS = "FROM Order";
    /**
     * The constant FIND_ORDER_BY_USER_ID.
     */
    public static final String FIND_ORDER_BY_USER_ID = "FROM Order o WHERE o.user.id = ?1";
    /**
     * The constant FIND_ORDER.
     */
    public static final String FIND_ORDER = "FROM Order o WHERE ";
    /**
     * The constant BY_COST.
     */
    public static final String BY_COST = "o.cost";
    /**
     * The constant BY_CREATING_DATE.
     */
    public static final String BY_CREATING_DATE = "o.createDate";
    /**
     * Instantiates a new Query storage.
     */
    private QueryStorage() {
    }
}