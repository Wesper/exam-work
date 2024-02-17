package ru.candle.store.orderservice.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    UNKNOWN_EXCEPTION("UNKNOWN_EXCEPTION","Произошла непредвиденная ошибка"),
    PROMOCODE_NOT_FOUND("PROMOCODE_NOT_FOUND","Промокод не найден"),
    PROMOCODE_NOT_ACTUAL("PROMOCODE_NOT_ACTUAL","Промокод не активен"),
    PROMOCODE_LIST_IS_EMPTY("PROMOCODE_LIST_IS_EMPTY","Список промокодоа пуст"),
    USER_BASKET_IS_EMPTY("USER_BASKET_IS_EMPTY","Корзина пуста"),
    GET_PRODUCTS_INFO_IS_NULL("GET_PRODUCTS_INFO_NULL","Информаци о продуктах не получена"),
    GET_USER_PROFILE_IS_NULL("GET_USER_PROFILE_IS_NULL","Профиль не получен"),
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND","Продукты пользователя не найдены"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND","Заказ не найден"),
    USER_DONT_HAVE_ORDERS("USER_DONT_HAVE_ORDERS","Заказы не найдены"),
    ORDER_IN_SEARCH_STATUS_NOT_FOUND("ORDER_IN_SEARCH_STATUS_NOT_FOUND","Заказы в запрашиваемом статусе не найдены"),
    PART_OF_PRODUCTS_NOT_ACTUAL("PART_OF_PRODUCTS_NOT_ACTUAL","Часть продуктов не актуальны, необходимо удалить их из корзины"),
    DELETE_PRODUCT_ERROR("DELETE_PRODUCT_ERROR","Ошибка при удалении продукта"),
    GET_PRODUCTS_INFO_NOT_COMPLETE("GET_PRODUCTS_INFO_NOT_COMPLETE","Получена информация не обо всех продуктах"),
    CHANGE_COUNT_PRODUCT_ERROR("CHANGE_COUNT_PRODUCT_ERROR","Не удалось изменить количество продуктов");

    private String errorCode;
    private String errorText;
}
