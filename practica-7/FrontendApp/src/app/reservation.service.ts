import { Injectable } from "@angular/core";
import { Reservation } from "./reservation";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { enviorment } from "../enviorments/enviorment";

@Injectable({
    providedIn: 'root'
})
export class ReservationService{
    private apiServerUrl = enviorment.apiBaseUrl;

    constructor(private http: HttpClient){}

    public getReservationsActive(): Observable<Reservation[]> {
        return this.http.get<Reservation[]>(`${this.apiServerUrl}/ReservationServerless/active`);
    }

    public getReservationsPasts(startDate: Date | null, endDate: Date | null): Observable<Reservation[]> {
 
        var url: string = '';
        
        if (startDate != null || endDate != null){
            const formattedStartDate: string = this.formatDate(startDate!);
            const formattedEndDate: string = this.formatDate(endDate!);

            url = `?startDate=${formattedStartDate}&endDate=${formattedEndDate}`;
        }    

        return this.http.get<Reservation[]>(`${this.apiServerUrl}/ReservationServerless/past${url}`);
    }

    public addReservation(reservation: Reservation): Observable<Reservation>{
        console.log(reservation)
        return this.http.post<Reservation>(`${this.apiServerUrl}/ReservationServerless`, reservation);
    }

    private formatDate(date: Date): string {
        const year: number = date.getFullYear();
        const month: number = date.getMonth() + 1; // January is 0!
        const day: number = date.getDate();
        const hours: string = ('0' + date.getHours()).slice(-2);
        const minutes: string = ('0' + date.getMinutes()).slice(-2);
        const seconds: string = ('0' + date.getSeconds()).slice(-2);
    
        return `${year}-${this.pad(month)}-${this.pad(day)}T${hours}:${minutes}:${seconds}`;
    }

    private pad(n: number): string {
        return n < 10 ? '0' + n : n.toString();
    }

    // public updateReservation(reservation: Reservation): Observable<Reservation>{
    //     return this.http.put<Reservation>(`${this.apiServerUrl}/reservations/update`, reservation);
    // }

    // public deleteReservation(reservationId: string): Observable<void>{
    //     return this.http.delete<void>(`${this.apiServerUrl}/reservations/delete/${reservationId}`);
    // }
}