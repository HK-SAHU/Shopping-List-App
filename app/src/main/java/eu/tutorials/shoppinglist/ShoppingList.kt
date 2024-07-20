package eu.tutorials.shoppinglist

import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp


data class ShoppingItem(val id: Int, var name: String, var quantity: Int, var isEditing: Boolean=false)


@Composable
fun ShoppingListApp(){
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember{ mutableStateOf(false) }
    var itemName by remember {mutableStateOf("")}
    var itemQuantity by remember{mutableStateOf("")}



    // here sItems will maintain the state of the list of item (remember)
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems) { 
//                ShoppingListItem(it,{},{})
                item ->
                if(item.isEditing){
                    ShoppingItemEditior(item = item, onEditComplete = {
                        editedName, editedQuantitiy ->
                        sItems=sItems.map{it.copy(isEditing = false)}
                        val editiedItem= sItems.find{it.id==item.id}   /*find function allows is to find a particular item inside of a list
                        the list we are looking through is sItems List. so, it finds the item that wwe are currently editing*/
                        editiedItem?.let{  // here we are updating the name of the items and quantity
                            it.name= editedName
                            it.quantity=editedQuantitiy
                        }
                    })
                }
                else{
                    ShoppingListItem(item = item, onEditClick = {
                        sItems= sItems.map { it.copy(isEditing = it.id==item.id) }   /*we are comparing the item to the shopping list element
                         that is passed to us inside of this map and we are comparing thiers id together and if they are true
                         then isEditing will be true otherwise it will be false*/

                        /*
                        * when we click on the edit button then how do we know which item edit button we have clicked
                        * so to find that out sItems= sItems.map { it.copy(isEditing = it.id==item.id) } we didi this
                        */
                    },
                        onDeleteClick = {
                        sItems=sItems-item
                    })
                }
            }
        }
        FloatingActionButton(
            onClick = { showDialog= true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Item")
        }
    }

    if(showDialog){
        AlertDialog(onDismissRequest = { showDialog= false },
            confirmButton = {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Button(onClick = { showDialog=false }) {
                                    Text("Cancel")
                                }

                                Button(onClick = {
                                    if(itemName.isNotBlank()){
                                        val newItem= ShoppingItem(
                                            id=sItems.size+1,
                                            name=itemName,
                                            quantity = itemQuantity.toInt())    // we are not passing isEditing because it is false by default
                                        sItems= sItems+newItem
                                        showDialog= false
                                        itemName=""   // after adding the item we reset the itemName with empty string so that it will not give the new name with continuation of old name
                                        itemQuantity=""
                                    }
                                }) {
                                    Text("Add")
                                }

                            }
            },
            title = { Text("Add Shopping Item")},
            text={
                Column{
                    OutlinedTextField(value = itemName,
                        onValueChange = {itemName= it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Enter Item Name")}
                    )

                    OutlinedTextField(value = itemQuantity,
                        onValueChange = {itemQuantity= it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Enter Quantity")}
                    )
                }

            }
            )
    }
}

@Composable
fun ShoppingItemEditior(item: ShoppingItem, onEditComplete: (String, Int)->Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
        ){
        Column {
//            BasicTextField(
//                value = editedName,
//                onValueChange = { editedName = it },
//                singleLine = true,
//                modifier = Modifier
//                    .wrapContentSize()
//                    .padding(8.dp)
//            )

            OutlinedTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

//            BasicTextField(
//                value = editedQuantity,
//                onValueChange = { editedQuantity = it },
//                singleLine = true,
//                modifier = Modifier
//                    .wrapContentSize()
//                    .padding(8.dp)
//            )

            OutlinedTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                isEditing=false
                onEditComplete(editedName, editedQuantity.toIntOrNull()?:1)// if the quantity is null then set it to 1
            }) {
                Text("Save")
            }
        }

    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick:()-> Unit,
    onDeleteClick:()-> Unit
){
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .border(
            border = BorderStroke(2.dp, Color(0XFF018786)),
            shape = RoundedCornerShape(20),
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier= Modifier.padding(8.dp)){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector=Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}


