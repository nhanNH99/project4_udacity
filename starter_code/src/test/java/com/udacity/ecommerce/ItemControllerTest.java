package com.udacity.ecommerce;

import com.udacity.ecommerce.controllers.ItemController;
import com.udacity.ecommerce.model.persistence.Item;
import com.udacity.ecommerce.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private final ItemRepository items = mock(ItemRepository.class);

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getItemByIdTest(){
        when(items.findById(1L)).thenReturn(Optional.of(createItem()));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        Item item = response.getBody();
        assertNotNull(item);
    }

    @Test
    public void getItemsTest(){
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> itemList = response.getBody();
        assertNotNull(itemList);
    }

    @Test
    public void getItemByNameTest(){
        List<Item> items = new ArrayList<>();

        items.add(createItem());
        when(this.items.findByName(Constant.TEST_ITEM)).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName(Constant.TEST_ITEM);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(items, response.getBody());
    }

    @Test
    public void getItemByNameFailWhenItemsIsNull(){
        when(this.items.findByName(Constant.TEST_ITEM)).thenReturn(null);
        ResponseEntity<List<Item>> response = itemController.getItemsByName(Constant.TEST_ITEM);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getItemByNameFailWhenItemsIsEmpty(){
        when(this.items.findByName(Constant.TEST_ITEM)).thenReturn(new ArrayList<>());
        ResponseEntity<List<Item>> response = itemController.getItemsByName(Constant.TEST_ITEM);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    public static Item createItem(){
        Item item = new Item();

        item.setId(1L);
        item.setName(Constant.TEST_ITEM);
        item.setDescription("This is a fake item for test.");
        item.setPrice(BigDecimal.valueOf(100.0));

        return item;
    }

}