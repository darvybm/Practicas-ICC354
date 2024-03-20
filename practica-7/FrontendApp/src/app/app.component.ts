import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Reservation } from './reservation';
// import { ReservationService } from './reservation.service';
import { HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { FormControl, FormGroup, FormsModule, NgForm, ReactiveFormsModule } from '@angular/forms';
import { ReservationService } from './reservation.service';
import { DataTablesModule } from 'angular-datatables';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { JsonPipe, NgIf } from '@angular/common';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, HttpClientModule, DataTablesModule, MatFormFieldModule, MatDatepickerModule, ReactiveFormsModule, JsonPipe, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  public reservations: Reservation[] = [];
  public editReservation: Reservation = {
    "id": "",
    "idUsuario": "",
    "nombre": "",
    "correo": "",
    "laboratorio": "",
    "horario": new Date(),
  };
  public deleteReservation: Reservation  = {
    "id": "",
    "idUsuario": "",
    "nombre": "",
    "correo": "",
    "laboratorio": "",
    "horario": new Date(),
  };

  fechaInicio: Date = new Date();
  fechaFin: Date = new Date();
  errorMessage: string = '';
  mostrarBotonAgregar: boolean = true;


  constructor(private reservationService: ReservationService){}

  ngOnInit(): void {

    this.getReservations();
  }

  public getReservations(): void {
    this.mostrarBotonAgregar = true;
    this.reservationService.getReservationsActive().subscribe({
        next: (response: Reservation[]) => {
          this.reservations = response;
        },

        error: (error: HttpErrorResponse) => {
          if (error instanceof HttpErrorResponse && error.error && error.error.errorMessage) {
            this.errorMessage = error.error.errorMessage;
            alert(this.errorMessage);
          } else {
            this.errorMessage = 'Ha ocurrido un error. Por favor, inténtalo de nuevo más tarde.';
            alert(this.errorMessage);
          }
        }
      }
    )
  }


  public onAddReservation(addForm: NgForm): void {
    // document.getElementById('add-reservations-form')?.click();
    this.reservationService.addReservation(addForm.value).subscribe({
      next: (response: Reservation) => {
        console.log(response);
        this.getReservations();
        addForm.reset();
      },
      error: (error: HttpErrorResponse) => {
        if (error instanceof HttpErrorResponse && error.error && error.error.errorMessage) {
          this.errorMessage = error.error.errorMessage;
          alert(this.errorMessage);
        } else {
          this.errorMessage = 'Ha ocurrido un error. Por favor, inténtalo de nuevo más tarde.';
        }
      }
    }     
    )
  }

  public onOpenModal(reservation: Reservation | null, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-bs-toggle', 'modal');
    if (mode === 'add'){
      button.setAttribute('data-bs-target', '#addReservationModal');
    }
    if (mode === 'filter'){
      button.setAttribute('data-bs-target', '#filterReservationModal');
    }
    container?.appendChild(button);
    button.click();
  }

  public getReservationsPasts(): void {
    this.mostrarBotonAgregar = false;
    this.reservationService.getReservationsPasts(null, null).subscribe({
        next: (response: Reservation[]) => {
          this.reservations = response;
        },

        error: (error: HttpErrorResponse) => {
          if (error instanceof HttpErrorResponse && error.error && error.error.errorMessage) {
            this.errorMessage = error.error.errorMessage;
            alert(this.errorMessage);
          } else {
            this.errorMessage = 'Ha ocurrido un error. Por favor, inténtalo de nuevo más tarde.';
            alert(this.errorMessage);
          }
        }
      }
    )
  }
  
  fetchReservationsPasts(startDate: Date, endDate: Date): void {
    this.reservationService.getReservationsPasts(startDate, endDate).subscribe({
      next: (response: Reservation[]) => {
        this.reservations = response;
      },

      error: (error: HttpErrorResponse) => {
        if (error instanceof HttpErrorResponse && error.error && error.error.errorMessage) {
          this.errorMessage = error.error.errorMessage;
          alert(this.errorMessage);
        } else {
          this.errorMessage = 'Ha ocurrido un error. Por favor, inténtalo de nuevo más tarde.';
          alert(this.errorMessage);
        }
      }
    });
  }

  onSubmitFilter(filterForm: NgForm): void {
    if (filterForm.valid) {
      this.fechaInicio = new Date(filterForm.value.fechaInicio);
      this.fechaFin = new Date(filterForm.value.fechaFin);
      console.log('Fecha filtradas');
      console.log(this.fechaInicio);
      console.log(this.fechaFin);

      // Llamando a la función para obtener las reservaciones pasadas
      this.fetchReservationsPasts(this.fechaInicio, this.fechaFin);
    }
  }

  formatDate(dateString: Date) {
    const date = new Date(dateString);
    return date.toLocaleString();
  }


}
