<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;
use App\userAdmin;
use Illuminate\Support\Facades\Auth;//INI UNTUK KEPENTINGAN LOGIN

class UserController extends Controller
{
    //password untuk user akan di pantek dengan udin123
    public function register(request $request){
        
        $user = new User;
        $user->user_id = $request->user_id;
        $user->password = $request->user_role == '1'? 'udin123' : $request->password;
        $user->password = bcrypt($user->password);
        $user->user_role = 1;
        $user->save();
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

    public function registerAdmin(request $request){
        
        $user = new User;
        $user->user_id = $request->user_name;
        $user->password = $request->password;
        $user->password = bcrypt($user->password);
        $user->user_role = 1;
        $user->save();

        $userAdmin = new userAdmin;
        $userAdmin->full_name = $request->full_name;
        $userAdmin->email = $request->email;
        $userAdmin->phone_number = $request->phone_number;
        $userAdmin->birth_date = $request->birth_date;
        $userAdmin->user_id = $user->id;
        $userAdmin->save();
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

    public function updateAdminUser(request $request){
        $user = User::whereUserId($request->user_name_old)->first();
        $userAdmin = userAdmin::find($user->id);
        $userAdmin->full_name = $request->full_name;
        $userAdmin->email = $request->email;
        $userAdmin->phone_number = $request->phone_number;
        $userAdmin->birth_date = $request->birth_date;
        $userAdmin->save();
        $user->user_id = $request->user_name;
        $user->save();
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

    public function deleteAdmin(request $request){
        $user = User::whereUserId($request->user_name)->first();
        $userAdmin = userAdmin::find($user->id);
        $userAdmin->delete();
        $user->delete();
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

    public function loginCustomer(request $request){
        $user = User::where('user_id',$request->user_id)->whereUserRole('1')->get();
        if(count($user)>0){
            if(Auth::attempt([
                'user_id'=>$request->user_id,
                'password'=>$request->password
            ])){
                return json_encode([
                    'is_error' => '0',
                    'message' => 'login berhasil'
                ]);
            }
            else{
                return json_encode([
                    'is_error' => '1',
                    'message' => 'user atau kata sandi salah'
                ]);
            }
        }
        else{
            return json_encode([
                'is_error' => '1',
                'message' => 'user atau kata sandi salah'
            ]);
        }        
    }

    public function loginAdmin(Request $request){
        $user = User::where('user_id',$request->user_id)->whereUserRole('0')->get();
        if(count($user)>0){
            if(Auth::attempt([
                'user_id'=>$request->user_id,
                'password'=>$request->password
            ])){
                return json_encode([
                    'is_error' => '0',
                    'message' => 'login berhasil'
                ]);
            }
            else{
                return json_encode([
                    'is_error' => '1',
                    'message' => 'user atau kata sandi salah'
                ]);
            }
        }
        else{
            return json_encode([
                'is_error' => '1',
                'message' => 'user atau kata sandi salah',
                'user_id' => $request
            ]);
        }        
    }

    public function getAllCustomerUser(){
        return json_encode([
            'result' => User::whereUserRole(1)->get() 
        ]);
    }
    
    public function getUserIdAndPasswordCustomer(request $request){
        $user = User::userId($request->user_id)->userRole(1)->get();
        if(count($user)>0){
            return json_encode([
                'is_error' => '0',
                'message' => 'berhasil',
                'user' => $user
            ]); 
        }

        return json_encode([
            'is_error' => '1',
            'message' => 'user tidak tersedia'
        ]);
    }

    public function testApi(request $request){
        return $request->test == null ? "test" : $request->test;
    }

    public function getAllAdminUser(){
        $users = User::whereUserRole(0)->get();
        $listAdmin = array();
        foreach($users as $user){
            $admin = $user->admin;
            $admin->user_name = $user->user_id;
            array_push($listAdmin,$admin);
            // $user->admin = json_decode(json_encode($user->admin));
        }
        return json_encode([
            'result' => $listAdmin
        ]);
    }
}
