package com.mexiti.garage360.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun MainTitle(title: String){
    Text(text= title, color = Color.White, fontWeight = FontWeight.Bold)
}

@Composable
fun MainTextField(value: String,
                  onValueChange: (String) -> Unit,
                  label: String){
    OutlinedTextField(value = value,
        onValueChange =onValueChange,
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp)
    )
}

@Composable
fun TaskCard(
    title: String,
    priority: Int,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min), // Ayuda a que la barra de color llene la altura
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra de color distintiva
            val color = when (priority) {
                1 -> Color(0xFF4CAF50) // Verde
                2 -> Color(0xFFCDDC39) // Lima
                3 -> Color(0xFFFFEB3B) // Amarillo
                4 -> Color(0xFFFF9800) // Naranja
                5 -> Color(0xFFF44336) // Rojo
                else -> Color.Gray
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(10.dp)
                    .background(color)
            )

            // Contenido de la tarjeta
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(text = "Prioridad: $priority")
            }

            // Check de completado
            if (isCompleted) {
                Text(
                    text = "✅",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    }
}

@Composable
fun PrioritySelector(
    currentPriority: Int,
    onPrioritySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val priorities = listOf(1, 2, 3, 4, 5)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Prioridad",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 30.dp, bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            priorities.forEach { priority ->
                PriorityDot(
                    priority = priority,
                    isSelected = (currentPriority == priority),
                    onClick = { onPrioritySelected(priority) }
                )
            }
        }
    }
}

@Composable
private fun PriorityDot(
    priority: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = when (priority) {
        1 -> Color(0xFF4CAF50) // Verde
        2 -> Color(0xFFCDDC39) // Lima
        3 -> Color(0xFFFFEB3B) // Amarillo
        4 -> Color(0xFFFF9800) // Naranja
        5 -> Color(0xFFF44336) // Rojo
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color, shape = CircleShape)
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color.Black else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onClick() }
    ) {
        Text(
            text = priority.toString(),
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Preview(showBackground = true, backgroundColor = 2345 )
@Composable
fun MainTitlePreview(){
    MainTitle(title = "TaskApp")
}

@Preview(showBackground = true)
@Composable
fun MainTextfieldPreview(){
    MainTextField(value = "Pagar internet", onValueChange ={} , label = "Tarea" )
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview(){
    TaskCard(title = "Hacer Práctica 3", priority = 5, isCompleted = false) {

    }
}
