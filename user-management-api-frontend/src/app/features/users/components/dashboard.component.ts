import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { User } from '../../../models/user.model';
import { AuthService } from '../../../authentication/service/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  private cdr = inject(ChangeDetectorRef);
  private userService = inject(UserService);
  private router = inject(Router);
  private authService = inject(AuthService);

  username = '';

  users: User[] = [];
  currentUserEmail = 'admin@demo.com';

  ngOnInit(): void {
    this.loadUsers();
    this.username = this.authService.getUsername() || 'Usuario';
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.cdr.detectChanges();
      },
      error: () => this.logout(),
    });
  }

  async createUser() {
    const { value: userData, isConfirmed } = await Swal.fire({
      title: 'Crear Usuario',
      html: `
      <input id="swal-username" class="swal2-input" placeholder="Usuario">
      <input id="swal-email" type="email" class="swal2-input" placeholder="Email">
      <input id="swal-password" type="password" class="swal2-input" placeholder="Contraseña">
      <select id="swal-estado" class="swal2-select">
        <option value="true" selected>Activo</option>
        <option value="false">Inactivo</option>
      </select>
    `,
      showCancelButton: true,
      confirmButtonText: 'Crear',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#0d6efd',
      focusConfirm: false,
      preConfirm: () => {
        const username = (
          document.getElementById('swal-username') as HTMLInputElement
        ).value.trim();
        const email = (document.getElementById('swal-email') as HTMLInputElement).value.trim();
        const password = (
          document.getElementById('swal-password') as HTMLInputElement
        ).value.trim();
        const estadoValue = (document.getElementById('swal-estado') as HTMLSelectElement).value;
        const estado = estadoValue === 'true';

        if (!username || !email || !password) {
          Swal.showValidationMessage('Usuario, email y contraseña son obligatorios');
          return;
        }

        return { username, email, password, estado };
      },
    });

    if (isConfirmed && userData) {
      try {
        await this.userService.createUser(userData).toPromise(); // espera la creación
        await Swal.fire('Creado', 'Usuario registrado correctamente', 'success');
        this.loadUsers(); // refresca la tabla inmediatamente
      } catch {
        Swal.fire('Error', 'No se pudo crear el usuario', 'error');
      }
    }
  }

  logout(): void {
    Swal.fire({
      title: 'Cerrar sesión',
      text: '¿Deseas salir del sistema?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Salir',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        localStorage.removeItem('authToken');
        this.router.navigate(['/login']);
      }
    });
  }

  viewUser(user: User) {
    Swal.fire({
      title: 'Detalle del Usuario',
      html: `
      <div style="text-align:left">
        <p><b>ID:</b> ${user.id}</p>
        <p><b>Usuario:</b> ${user.username}</p>
        <p><b>Email:</b> ${user.email}</p>
        <p><b>Estado:</b> ${user.estado ? 'Activo' : 'Inactivo'}</p>
        <p><b>Fecha de creación:</b> 
          ${new Date(user.fechaCreacion).toLocaleString()}
        </p>
      </div>
    `,
      icon: 'info',
      confirmButtonText: 'Cerrar',
      confirmButtonColor: '#0d6efd',
    });
  }

  copyPassword(password: string) {
    navigator.clipboard
      .writeText(password)
      .then(() => {
        Swal.fire({
          toast: true,
          position: 'top-end',
          icon: 'success',
          title: 'Contraseña copiada',
          showConfirmButton: false,
          timer: 1500,
        });
      })
      .catch(() => {
        Swal.fire({
          toast: true,
          position: 'top-end',
          icon: 'error',
          title: 'No se pudo copiar',
          showConfirmButton: false,
          timer: 1500,
        });
      });
  }

  editUser(user: User) {
    Swal.fire({
      title: 'Editar Usuario',
      html: `
      <input id="swal-username" class="swal2-input" placeholder="Usuario" value="${user.username}">
      <input id="swal-email" type="email" class="swal2-input" placeholder="Email" value="${
        user.email
      }">
      <input id="swal-password" type="password" class="swal2-input" placeholder="Nueva contraseña (opcional)">
      <select id="swal-estado" class="swal2-select">
        <option value="true" ${user.estado ? 'selected' : ''}>Activo</option>
        <option value="false" ${!user.estado ? 'selected' : ''}>Inactivo</option>
      </select>
    `,
      showCancelButton: true,
      confirmButtonText: 'Guardar',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#0d6efd',
      focusConfirm: false,
      preConfirm: () => {
        const username = (
          document.getElementById('swal-username') as HTMLInputElement
        ).value.trim();
        const email = (document.getElementById('swal-email') as HTMLInputElement).value.trim();
        const password = (
          document.getElementById('swal-password') as HTMLInputElement
        ).value.trim();
        const estadoValue = (document.getElementById('swal-estado') as HTMLSelectElement).value;
        const estado = estadoValue === 'true';

        if (!username || !email) {
          Swal.showValidationMessage('Usuario y email son obligatorios');
          return;
        }

        const payload: any = { username, email, estado };
        if (password) payload.password = password;

        return payload;
      },
    }).then((result) => {
      if (result.isConfirmed) {
        this.userService.updateUser(user.id!, result.value).subscribe({
          next: () => {
            Swal.fire('Actualizado', 'Usuario modificado correctamente', 'success');
            this.loadUsers();
          },
          error: () => {
            Swal.fire('Error', 'No se pudo actualizar el usuario', 'error');
          },
        });
      }
    });
  }

  deleteUser(id: number, email: string) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `Eliminar usuario ${email}`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.userService.deleteUser(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Eliminado',
              text: 'El usuario fue eliminado correctamente',
              timer: 1500,
              showConfirmButton: false,
            });
            this.loadUsers();
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo eliminar el usuario',
            });
          },
        });
      }
    });
  }
}
